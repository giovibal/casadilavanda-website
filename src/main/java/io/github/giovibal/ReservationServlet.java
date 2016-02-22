package io.github.giovibal;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * Created by giova_000 on 15/04/2015.
 */
public class ReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        URL url = new URL(request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath());
        String lang = request.getLocale().getLanguage();
        String redirectUrl;
        if("de".equalsIgnoreCase(lang)) {
            redirectUrl = url.toExternalForm() + "/de/";
        } else {
            redirectUrl = url.toExternalForm() + "/en/";
        }


        String to = "feriencasalavanda@gmail.com";
        String toBCC = "giovibal@gmail.com";

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        PostInfo pinfo = getInfo(request);
        String msgBody = getBody(pinfo);
        try {
            Message msg = new MimeMessage(session);
            //msg.addHeader("Sender", "giovibal@gmail.com");
            msg.setReplyTo(new Address[]{new InternetAddress(pinfo.email, pinfo.name + " " + pinfo.surname)});
            msg.setFrom(new InternetAddress("giovibal@gmail.com", "casadilavanda.com"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            msg.addRecipient(Message.RecipientType.BCC,
                    new InternetAddress(toBCC));
            msg.setSubject("Reservation from casadilavanda.com");
            msg.setText(msgBody);
            Transport.send(msg);
            log("Reservation done: from " + pinfo + " message " + msgBody);
            saveReservation(pinfo);
            response.sendRedirect(redirectUrl + "reservation_ok.html");
        } catch (AddressException e) {
            log(e.getMessage(), e);
            response.sendRedirect(redirectUrl + "reservation_problem.html");
        } catch (MessagingException e) {
            log(e.getMessage(), e);
            response.sendRedirect(redirectUrl + "reservation_problem.html");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getBody(PostInfo postInfo) {
        // COSTRUZIONE DEL CORPO DEL MESSAGGIO
        String s = postInfo.getEmailBody();
        return s;
    }

    private PostInfo getInfo(HttpServletRequest request) {
        PostInfo pi = new PostInfo();
        pi.init(request);
        return pi;
    }

    static class PostInfo {
        void init(HttpServletRequest request) {
            name = request.getParameter("name");
            surname = request.getParameter("surname");
            city = request.getParameter("city");
            country = request.getParameter("country");
            state = request.getParameter("state");
            telephone = request.getParameter("telephone");
            email = request.getParameter("email");
            arrival = request.getParameter("arrival");
            departure = request.getParameter("departure");
            numnights = request.getParameter("numnights");
            numpersons = request.getParameter("numpersons");
            numchildren = request.getParameter("numchildren");
            message = request.getParameter("message");
        }
        String name;
        String surname;
        String city;
        String country;
        String state;
        String telephone;
        String email;
        String arrival;
        String departure;
        String numnights;
        String numpersons;
        String numchildren;
        String message;

        @Override
        public String toString() {
            return name +" "+ surname +" "+ email;
        }

        public String getEmailBody() {
            // COSTRUZIONE DEL CORPO DEL MESSAGGIO
            String s = "New Reservation Request from casadilavanda.com; \n";
            s += "Name: " + name  + "\n";
            s += "Surname: " + surname  + "\n";
            s += "City: " + city + "\n";
            s += "Country: " + country + "\n";
            s += "State: " + state+ "\n";
            s += "Telephone: " + telephone + "\n";
            s += "E-Mail: " + email + "\n";
            s += "Arrival on: " + arrival + "\n";
            s += "Departure on: " + departure + "\n";
            s += "Number of night(s): " + numnights + "\n";
            s += "Number of person(s): " + numpersons + "\n";
            s += "Number of children: " + numchildren + "\n";
            s += "Message: " + message + "\n";
            return s;
        }
    }


    private void saveReservation(PostInfo postInfo) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity reservation = new Entity("Reservation");
        reservation.setProperty("name", postInfo.name);
        reservation.setProperty("name", postInfo.name);
        reservation.setProperty("surname", postInfo.surname);
        reservation.setProperty("city", postInfo.city);
        reservation.setProperty("country", postInfo.country);
        reservation.setProperty("state", postInfo.state);
        reservation.setProperty("telephone", postInfo.telephone);
        reservation.setProperty("email", postInfo.email);
        reservation.setProperty("arrival", postInfo.arrival);
        reservation.setProperty("departure", postInfo.departure);
        reservation.setProperty("numnights", postInfo.numnights);
        reservation.setProperty("numpersons", postInfo.numpersons);
        reservation.setProperty("numchildren", postInfo.numchildren);
        reservation.setProperty("message", postInfo.message);
        Date insertDate = new Date();
        reservation.setProperty("insertDate", insertDate);

        datastore.put(reservation);
    }
}
