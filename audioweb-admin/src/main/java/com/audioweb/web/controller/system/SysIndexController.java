package com.audioweb.web.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.audioweb.common.config.Global;
import com.audioweb.common.core.controller.BaseController;
import com.audioweb.framework.util.ShiroUtils;
import com.audioweb.framework.web.domain.Server;
import com.audioweb.server.service.IClientService;
import com.audioweb.system.domain.SysMenu;
import com.audioweb.system.domain.SysUser;
import com.audioweb.system.service.ISysMenuService;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private IClientService clientService;
    
    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled", Global.isDemoEnabled());
        mmap.put("systemtime",df.format(new Date()));
        
        /*clientService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while(true) {
						clientService.executeAsync();
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
        return "index";
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        Server server = new Server();
        try {
			server.copyTo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mmap.put("server", server);
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
