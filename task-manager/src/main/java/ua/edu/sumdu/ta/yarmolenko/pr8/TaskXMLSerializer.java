package ua.edu.sumdu.ta.yarmolenko.pr8;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Class to maintain state of implementation class AbstractTaskList to document
 */
public class TaskXMLSerializer {

    /**
     * Method fo saving a task list object to XML file
     *
     * @param object is an object of AbstractTaskList data type
     * @param file is a file for saving
     */
    public void save(AbstractTaskList object, String file) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element tasksElement = document.createElement("tasks");
        document.appendChild(tasksElement);

        for (Task taskListItem : object) {
            Element taskElement = document.createElement("task");
            taskElement.setAttribute("active", String.valueOf(taskListItem.isActive()));
            taskElement.setAttribute("time", String.valueOf(taskListItem.getTime()));
            taskElement.setAttribute("start", taskListItem.isRepeated() ? String.valueOf(taskListItem.getStartTime()) : "");
            taskElement.setAttribute("end", taskListItem.isRepeated() ? String.valueOf(taskListItem.getEndTime()) : "");
            taskElement.setAttribute("repeat", String.valueOf(taskListItem.getRepeatInterval()));
            taskElement.setAttribute("repeated", String.valueOf(taskListItem.isRepeated()));
            taskElement.appendChild(document.createTextNode(taskListItem.getTitle()));
            tasksElement.appendChild(taskElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(file));

        transformer.transform(domSource, streamResult);
    }

    /**
     * Method fo loading a task list object from XML file
     *
     * @param file is a file for loading
     * @return an object of AbstractTaskList data type
     */
    public AbstractTaskList load(String file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(file));

        NodeList elements = document.getElementsByTagName("task");

        AbstractTaskList taskList = new ArrayTaskList();

        for (int i = 0; i < elements.getLength(); i++) {
            NamedNodeMap attributes = elements.item(i).getAttributes();
            String title = elements.item(i).getTextContent();
            String active = attributes.getNamedItem("active").getNodeValue();
            String time = attributes.getNamedItem("time").getNodeValue();
            String start = attributes.getNamedItem("start").getNodeValue();
            String end = attributes.getNamedItem("end").getNodeValue();
            String repeat = attributes.getNamedItem("repeat").getNodeValue();
            String repeated = attributes.getNamedItem("repeated").getNodeValue();

            boolean b = ((active.trim()).equals("true") ? true : false);
            if ((repeated.trim()).equals("true")) {
                Task task = new Task(title, Integer.parseInt(start), Integer.parseInt(end), Integer.parseInt(repeat));
                if (b) {
                    task.setActive(true);
                }
                taskList.add(task);
            } else {
                Task task = new Task(title, Integer.parseInt(time));
                if (b) {
                    task.setActive(true);
                }
                taskList.add(task);
            }


        }

        return taskList;
    }
}
