package com.audioweb.serverPool.service;

public interface IClientService {
	/**
     *  执行异步任务
     *  可以根据需求，自己加参数拟定
     */
    void executeAsync();
    
    void execute(Runnable task);
}
