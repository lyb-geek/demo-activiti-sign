package com.demo.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class SignActivitiMain {
	public static void main(String[] args) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		RepositoryService repositoryService = processEngine.getRepositoryService();

		RuntimeService runtimeService = processEngine.getRuntimeService();

		TaskService taskService = processEngine.getTaskService();

		IdentityService identityService = processEngine.getIdentityService();

		identityService.setAuthenticatedUserId("worker");

		Deployment deployment = repositoryService.createDeployment().addClasspathResource("sign.bpmn").deploy();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

		Map<String, Object> vars = new HashMap<>();
		List<String> leaderList = new ArrayList<>();
		leaderList.add("zhangsan");
		leaderList.add("lisi");
		leaderList.add("wangwu");

		vars.put("leaderList", leaderList);

		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), vars);

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

		System.out.println("taskName1:" + task.getName() + "|assignee:" + task.getAssignee());

		taskService.complete(task.getId());

		Map<String, Object> leaderOneAudit = new HashMap<>();
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入领导审批意见。。。。");
		String auditOne = scanner.nextLine();
		leaderOneAudit.put("audit", auditOne);
		System.out.println("张三的审批意见为：" + (auditOne.equals("yes") ? "同意" : "不同意"));
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("zhangsan").singleResult();
		taskService.complete(task.getId(), leaderOneAudit);
		System.out.println("taskName2:" + task.getName() + "|assignee:" + task.getAssignee());

		System.out.println("--------------------------------------------");
		Map<String, Object> leaderTwoAudit = new HashMap<>();
		System.out.println("请输入领导审批意见。。。。");
		String auditTwo = scanner.nextLine();
		leaderTwoAudit.put("audit", auditTwo);
		System.out.println("李四的审批意见为：" + (auditTwo.equals("yes") ? "同意" : "不同意"));

		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("lisi").singleResult();
		taskService.complete(task.getId(), leaderTwoAudit);
		System.out.println("taskName3:" + task.getName() + "|assignee:" + task.getAssignee());

		System.out.println("--------------------------------------------");
		Map<String, Object> leaderThreeAudit = new HashMap<>();
		System.out.println("请输入领导审批意见。。。。");
		String auditThree = scanner.nextLine();
		leaderThreeAudit.put("audit", auditThree);
		System.out.println("李四的审批意见为：" + (auditThree.equals("yes") ? "同意" : "不同意"));
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("wangwu").singleResult();
		taskService.complete(task.getId(), leaderThreeAudit);
		System.out.println("taskName4:" + task.getName() + "|assignee:" + task.getAssignee());

		// List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		if ("yes".equals(auditOne) && "yes".equals(auditTwo) && "yes".equals(auditThree)) {
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("hr").singleResult();
			taskService.complete(task.getId());
			System.out.println("taskName5:" + task.getName() + "|assignee:" + task.getAssignee());
		}
		// for (Task task2 : tasks) {
		// //taskService.complete(task2.getId());
		// System.out.println("taskName:" + task2.getName() + "|assignee:" + task2.getAssignee());
		// }

		System.out.println("----------------------------------流程实例流转-----------------------");
		// List<HistoricActivityInstance> list = processEngine.getHistoryService() // 历史相关Service
		// .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
		// .processInstanceId(processInstance.getId()) // 执行流程实例id
		// .finished().list();
		// for (HistoricActivityInstance hai : list) {
		// System.out.println("活动ID:" + hai.getId());
		// System.out.println("流程实例ID:" + hai.getProcessInstanceId());
		// System.out.println("活动名称：" + hai.getActivityName());
		// System.out.println("办理人：" + hai.getAssignee());
		// System.out.println("开始时间：" + hai.getStartTime());
		// System.out.println("结束时间：" + hai.getEndTime());
		// System.out.println("=================================");
		// }

		List<HistoricTaskInstance> list = processEngine.getHistoryService() // 历史相关Service
				.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.processInstanceId(processInstance.getId()) // 用流程实例id查询
				.finished() // 查询已经完成的任务
				.list();
		for (HistoricTaskInstance hti : list) {
			System.out.println("任务ID:" + hti.getId());
			System.out.println("流程实例ID:" + hti.getProcessInstanceId());
			System.out.println("任务名称：" + hti.getName());
			System.out.println("办理人：" + hti.getAssignee());
			System.out.println("开始时间：" + hti.getStartTime());
			System.out.println("结束时间：" + hti.getEndTime());
			System.out.println("=================================");
		}

		processEngine.close();
	}

}
