package ua.edu.sumdu.ta.yarmolenko.pr8.tests;

import org.xml.sax.SAXException;
import ua.edu.sumdu.ta.yarmolenko.pr8.*;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.junit.Assert.*;

public class TaskXMLSerializerTest {

    @Test
    public void testXMLSerializer() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        String fileName = "TaskXMLSerializer.xml";
        Task t1 = new Task("Проснуться утром", 28800);
        t1.setActive(true);
        Task t2 = new Task("Покушать (завтрак, обед, ужин)", 30000,64800, 14400);
        t2.setActive(true);
        AbstractTaskList taskList1 = new ArrayTaskList();
        taskList1.add(t1);
        taskList1.add(t2);
        TaskXMLSerializer taskSerializer = new TaskXMLSerializer();

        taskSerializer.save(taskList1, fileName);

        AbstractTaskList taskList2 = taskSerializer.load(fileName);

        System.out.println("tl_1: " + taskList1.toString());
        System.out.println("tl_2: " + taskList2.toString());

        assertEquals(taskList1,taskList1);
    }
}
