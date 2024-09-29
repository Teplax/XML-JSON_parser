package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        //Зададим имя xml-файла со списком сотрудников
        String fileName="data.xml";

        //Считаем сотрудников из файла с помощью метода parseXML и запишем их в список
        List<Employee> list = parseXML(fileName);

        //преобразуем полученный список  в строчку в формате JSON
        String json = listToJson(list);
        System.out.println(json);

        //создадим переменную с именем json-файла
        String json_out= "data.json";
        //Запишем строку в файл:
        writeString(json,json_out);

    }

    //метод записи json-строки в файл
    public static void writeString(String json_string, String json_file){

        //создадим объект типа FileWriter в блоке try с ресурсами,
        // передав ему имя файла в параметр конструктора
        try (FileWriter file = new
                FileWriter(json_file)) {
            //вызовем метод write, передав в него json-строку
            file.write(json_string);            //
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        //Создаём экземпляр класса GsonBuilder и с помощью билдера создаём объект gson
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //вызываем у объекта gson метод для формирования строки из списка сотрудников
        return gson.toJson(list);
    }

    private static List<Employee> parseXML(String xmlfile) throws ParserConfigurationException, IOException, SAXException {
        //Создадим пустой список сотрудников
        List<Employee> employees = new ArrayList<>();

        //Получим экземпляр класса Document с использованием DocumentBuilderFactory и DocumentBuilder
        // через метод parse()
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlfile));

        //Получим корневой узел документа:
        Node root = doc.getDocumentElement();
        System.out.println("Корневой элемент: " + root.getNodeName());

        //Получим список дочерних узлов:
        NodeList nodeList = root.getChildNodes();

        //Пройдёмся по списку узлов и получим из каждого из них Element
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node_ = nodeList.item(i);
            //если текущий узел "employee"
            if (node_.getNodeName().equals("employee")) {
                //приведём узел к элементу
                Element emp = (Element) node_;

                //создадим экземпляр класса сотрудника
                Employee temp = new Employee();

                //заполним поля из соответствующих значений элемента:
                temp.id= Long.parseLong(emp.getElementsByTagName( "id").item(0).getTextContent());
                temp.firstName=emp.getElementsByTagName( "firstName").item(0).getTextContent();
                temp.lastName=emp.getElementsByTagName( "lastName").item(0).getTextContent();
                temp.country=emp.getElementsByTagName( "country").item(0).getTextContent();
                temp.age= Integer.parseInt(emp.getElementsByTagName( "age").item(0).getTextContent());

                //добавим полученного сотрудника в список
                employees.add(temp);
            }
        }
        return employees;
    }


}