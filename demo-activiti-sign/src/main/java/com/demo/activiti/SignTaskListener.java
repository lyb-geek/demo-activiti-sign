package com.demo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class SignTaskListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		DelegateExecution execution = delegateTask.getExecution();
		System.out.println("流程实例总数：" + execution.getVariable("nrOfInstances"));
		System.out.println("当前活动的流程实例总数：" + execution.getVariable("nrOfActiveInstances"));
		System.out.println("已经完成实例的数目：" + execution.getVariable("nrOfCompletedInstances"));
		System.out.println("leader：" + execution.getVariable("leader"));
		System.out.println("---------------------------------------分隔线----------------------------------------------------");

	}

}
