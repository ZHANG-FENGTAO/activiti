package com.example.activiti;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ActivitiApplication.class})
public class ActivitiApplicationTests {


    private static Logger logger = LoggerFactory.getLogger(ActivitiApplicationTests.class);

    @Autowired
    private ProcessEngineConfiguration configuration;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;


    @Test
    public void contextLoads() {
        Group group = identityService.newGroup("user");
        group.setName("users");
        group.setType("security-role");
        identityService.saveGroup(group);

        User admin = identityService.newUser("admin");
        admin.setPassword("admin");
        identityService.saveUser(admin);
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deployTest() {

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/helloWorld.bpmn20.xml").deploy();

        logger.info("activiti deploy id----->{}, name----->{}", deployment.getId(), deployment.getName());
        logger.info("Number of process definitions: {}", repositoryService.createProcessDefinitionQuery().count());

    }


    /**
     * 启动流程定义
     */
    @Test
    public void startTest() {

        ProcessInstance run = runtimeService.startProcessInstanceByKey("HelloWorld");
        logger.info("流程实例ID:{}", run.getId());
        logger.info("流程定义ID:{}", run.getProcessDefinitionId());
        logger.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());

    }


    /**
     * 完成流程
     */
    @Test
    public void completeTest() {

        String assignee = "张三";
        //查询指定办理人的任务列表
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            }
        }

        //完成任务
        if (list != null && list.size() > 0) {
            Task task = list.get(0);
            taskService.complete(task.getId());
        }


    }

}

