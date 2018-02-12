package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.servlet.annotation.WebServlet(name = "MainServlet", urlPatterns = "/MainServlet", description = " ")
public class MainServlet extends javax.servlet.http.HttpServlet {

    public static final String ACTION = "action";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MESSAGE = "message";

    private String currentMessage;
    private User currentUser;
    private Map<String, User> usersMap;
    private JSONObject jsonMessage;

    @Override
    public void init() throws ServletException {
        super.init();
        usersMap = new HashMap<>();
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("in doPost()");

        InputStream inputStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int actuallyRead;
        while ((actuallyRead = inputStream.read(buffer)) != -1)
            sb.append(new String(buffer, 0, actuallyRead));
        inputStream.close();
        inputStream = null;
        System.out.println();

        if (currentUser != null) {
            try {
                jsonMessage = new JSONObject(sb.toString());
                System.out.println(jsonMessage.toString());
                currentUser = usersMap.get(jsonMessage.getString(USERNAME));
                System.out.println("currentusers name been set " /*+ currentUser.getUsername()*/);
                currentMessage = jsonMessage.getString(MESSAGE);

                System.out.println("currentUser: " + currentUser.getUsername()); /*+ "\n current message: "
                    + currentUser.lstMessages.get(currentUser.getCount() - 1*/
                if (!currentMessage.isEmpty()) {
                    currentUser.lstMessages.add(currentMessage);
                    OutputStream outputStream = response.getOutputStream();
                    outputStream.write("ok".getBytes());
                    outputStream.close();
                    outputStream = null;
                }
                System.out.println("count is: " + currentUser.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("in doGet");
        String action = "", username = "", password = "", queryString;
        queryString = request.getQueryString();
        if (!queryString.isEmpty()) {
            String[] kav = queryString.split("&");
            for (String s : kav) {
                String[] kov = s.split("=");
                switch (kov[0]) {
                    case ACTION:
                        action = kov[1];
                        break;
                    case USERNAME:
                        username = kov[1];
                        break;
                    case PASSWORD:
                        password = kov[1];
                        break;
                }
            }
            System.out.println("action: " + action + ", username: " + username + ", password: " + password);
            if (action.equals("signup")) {
                System.out.println("##do Signup##");
                if (usersMap.containsKey(username)) {
                    response.getWriter().write("Username already exists");
                } else {
//                    currentUser = new User(username, password);
                    usersMap.put(username, new User(username, password));
                    if (usersMap.containsKey(username)) {
                        response.getWriter().write("User has been added");
                    }
                    System.out.println("response: has been added");
                    for (int i = 0; i < usersMap.size(); i++) {
                    }
                }
            } else if (action.equals("login")) {
                System.out.println("##do Login##");
                if (usersMap.containsKey(username)) {
                    if (password.equals(usersMap.get(username).getPassword())) {
                        currentUser = usersMap.get(username);
                        response.getWriter().write("ok");
                        System.out.println("response: ok");
                    } else response.getWriter().write("One of the details is incorrect");
                    System.out.println("ootdii");
                }else{
                    response.getWriter().write("You should sign up first");
                    System.out.println("response: You should sign up first");
                }
            }
        }
    }

    static class User {
        String username;
        String password;
        List<String> lstMessages;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            lstMessages = new ArrayList<>();
        }

        public int getCount() {
            return lstMessages.size();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getLstMessages() {
            return lstMessages;
        }

        public void setLstMessages(List<String> lstMessages) {
            this.lstMessages = lstMessages;
        }
    }
}
