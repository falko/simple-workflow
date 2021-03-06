package org.camunda.bpm.swf;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.junit.Before;
import org.junit.Test;

public class CamundaBpmTransformerTest
{

    public static final String MODEL_FILENAME = "/camunda-bpm/external-task.yaml";
    public static final String MODEL_USER_TASK = "/camunda-bpm/userTaskModel.yaml";

    private Transformer transformer;

    @Before
    public void prepare() {
      transformer = new Transformer(new CamundaBpmTaskFactory());
    }

    @Test
    public void testSimpleModel() throws FileNotFoundException {

        final BpmnModelInstance modelInstance = transformer.transform(CamundaBpmTransformerTest.class.getResourceAsStream(MODEL_FILENAME));

        ServiceTask serviceTask = modelInstance.getModelElementById("TASK_1");

        assertEquals(serviceTask.getCamundaTaskPriority(), "10");
        assertEquals(serviceTask.getCamundaTopic(), "someTopic");
    }

    @Test
    public void testUserTasks()
    {
        final InputStream inputStream = this.getClass().getResourceAsStream(MODEL_USER_TASK);

        final BpmnModelInstance modelInstance = transformer.transform(inputStream);
        System.out.println(Bpmn.convertToString(modelInstance));

        assertNotNull(modelInstance.getModelElementById("OrderProcessingWithUserTasks"));

    }


}
