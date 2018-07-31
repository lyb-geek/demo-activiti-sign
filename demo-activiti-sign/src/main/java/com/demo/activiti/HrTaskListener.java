package com.demo.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class HrTaskListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {

		delegateTask.setAssignee("hr");

	}

}
