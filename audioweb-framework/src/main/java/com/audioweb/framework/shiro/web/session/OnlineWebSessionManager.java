package com.audioweb.framework.shiro.web.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.audioweb.common.constant.ShiroConstants;
import com.audioweb.common.global.WebsocketGlobal;
import com.audioweb.common.utils.StringUtils;
import com.audioweb.common.utils.bean.BeanUtils;
import com.audioweb.common.utils.spring.SpringUtils;
import com.audioweb.framework.shiro.session.OnlineSession;
import com.audioweb.system.domain.SysUserOnline;
import com.audioweb.system.service.ISysUserOnlineService;

/**
 * 主要是在此如果会话的属性修改了 就标识下其修改了 然后方便 OnlineSessionDao同步
 * 
 * @author ruoyi,shuofang
 */
public class OnlineWebSessionManager extends DefaultWebSessionManager
{
	/**默认提前查询16分钟的超时信息,用于及时更新挂机会话信息*/
	private static final Integer AHEAD_TIME = 12*60*1000;
    private static final Logger log = LoggerFactory.getLogger(OnlineWebSessionManager.class);

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException
    {
        super.setAttribute(sessionKey, attributeKey, value);
        if (value != null && needMarkAttributeChanged(attributeKey))
        {
            OnlineSession session = getOnlineSession(sessionKey);
            session.markAttributeChanged();
        }
    }

    private boolean needMarkAttributeChanged(Object attributeKey)
    {
        if (attributeKey == null)
        {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework"))
        {
            return false;
        }
        if (attributeKeyStr.startsWith("javax.servlet"))
        {
            return false;
        }
        if (attributeKeyStr.equals(ShiroConstants.CURRENT_USERNAME))
        {
            return false;
        }
        return true;
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException
    {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null)
        {
            OnlineSession s = getOnlineSession(sessionKey);
            s.markAttributeChanged();
        }

        return removed;
    }

    public OnlineSession getOnlineSession(SessionKey sessionKey)
    {
        OnlineSession session = null;
        Object obj = doGetSession(sessionKey);
        if (StringUtils.isNotNull(obj))
        {
            session = new OnlineSession();
            BeanUtils.copyBeanProp(session, obj);
        }
        return session;
    }

    /**
     * 验证session是否有效 用于删除过期session
     */
    @Override
    public void validateSessions()
    {
        if (log.isInfoEnabled())
        {
            log.info("invalidation sessions...");
        }

        int invalidCount = 0;

        int timeout = (int) this.getGlobalSessionTimeout();
        Date expiredDate = DateUtils.addMilliseconds(new Date(), 0 - timeout + AHEAD_TIME);
        ISysUserOnlineService userOnlineService = SpringUtils.getBean(ISysUserOnlineService.class);
        List<SysUserOnline> userOnlineList = userOnlineService.selectOnlineByExpired(expiredDate);
        // 批量过期删除
        List<String> needOfflineIdList = new ArrayList<String>();
        //webSocket在线用户忽略
        List<String> webSocketIds = WebsocketGlobal.getAllIds();
        for(String id:webSocketIds) {
        	for(SysUserOnline online:userOnlineList) {
        		if(online.getSessionId().equals(id)) {
        			userOnlineList.remove(online);
        			/**手动更新session最后会话信息，防止掉线*/
        			//OnlineSession session = getOnlineSession(new DefaultSessionKey(online.getSessionId()));
        			OnlineSession session = null;
    		        Object obj = doGetSession(new DefaultSessionKey(online.getSessionId()));
    		        if (StringUtils.isNotNull(obj))
    		        {
    		        	try {
    		        		session = (OnlineSession)obj;
    		        		session.touch();
						} catch (Exception e) {
							e.printStackTrace();
							log.error("出错");
						}
    		        }
        			break;
        		}
        	}
        }
        /** 并未超时用户忽略 */
        for (SysUserOnline userOnline : userOnlineList)
        {
            try
            {
            	if(userOnline.getLastAccessTime().before(DateUtils.addMilliseconds(new Date(), 0 - timeout))) {
	                SessionKey key = new DefaultSessionKey(userOnline.getSessionId());
	                Session session = retrieveSession(key);
	                if (session != null)
	                {
	                    throw new InvalidSessionException();
	                }
            	}
            }
            catch (InvalidSessionException e)
            {
                if (log.isDebugEnabled())
                {
                    boolean expired = (e instanceof ExpiredSessionException);
                    String msg = "Invalidated session with id [" + userOnline.getSessionId() + "]"
                            + (expired ? " (expired)" : " (stopped)");
                    log.debug(msg);
                }
                invalidCount++;
                needOfflineIdList.add(userOnline.getSessionId());
            }

        }
        if (needOfflineIdList.size() > 0)
        {
            try
            {
                userOnlineService.batchDeleteOnline(needOfflineIdList);
            }
            catch (Exception e)
            {
                log.error("batch delete db session error.", e);
            }
        }

        if (log.isInfoEnabled())
        {
            String msg = "Finished invalidation session.";
            if (invalidCount > 0)
            {
                msg += " [" + invalidCount + "] sessions were stopped.";
            }
            else
            {
                msg += " No sessions were stopped.";
            }
            log.info(msg);
        }

    }

    @Override
    protected Collection<Session> getActiveSessions()
    {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }
}
