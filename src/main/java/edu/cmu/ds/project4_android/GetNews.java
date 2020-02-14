package edu.cmu.ds.project4_android;


import android.os.AsyncTask;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * @author Sheryl Hsiung.
 * This class provides capabilities to input amount of news it wants to read from a public API.
 * The method "search" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore,
 * this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  However, any UI updates should be done in the UI thread
 * to avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the newsReady method to do the update.
 * <p>
 * Note!!! Please wait for a few minutes when the app is launshed
 * and wait for its response at first initiation! It is fetching.
 * The button will turn from blue to grey when info is fetched.
 * <p>
 * <p>
 * Note: task 1 heroku path => https://salty-castle-05415.herokuapp.com/
 * Note: task 2 heroku path => https://glacial-gorge-63785.herokuapp.com/
 * Note: Unique analysis heroku path => https://glacial-gorge-63785.herokuapp.com/getAnalysis
 */
public class GetNews {
    /**
     * a reference to InterestingNews object
     */
    InterestingNews ip = null;
    /**
     * the user input amount.
     */
    private int amount;
    /**
     * the type of the school, CMU or other.
     */
    private String type;

    /**
     * search is the public GetNews method.
     * Its arguments are the parameters, and the InterestingNews object calls it.
     */
    public void search(String pictureUrl, InterestingNews ip, int amount, String type) {
        this.ip = ip;
        this.amount = amount;
        this.type = type;
        new AsyncSearch().execute(pictureUrl);
    }


    /**
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncSearch extends AsyncTask<String, Void, StringBuilder> {
        /**
         * this method getNews using a http get to request news
         * and read the response from the server and set the result to the response.
         *
         * @param result is the obj to be set.
         * @return a int indicate the status.
         */
        public int getNews(Result result) {
            // Make an HTTP GET passing the name on the URL line
            result.setValue("");
            String response = "";
            HttpURLConnection conn = null;
            int status;
            while (true) {
                try {
                    // pass the name on the URL line
                    //task1: https://salty-castle-05415.herokuapp.com/
                    //default: https://glacial-gorge-63785.herokuapp.com/toAndroid
                    URL url = new URL("https://glacial-gorge-63785.herokuapp.com/toAndroid");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // tell the server what format we want back
                    conn.setRequestProperty("Accept", "text/plain");
                    // wait for response
                    status = conn.getResponseCode();
                    System.out.println("status: " + status);
                    // If things went poorly, don't try to read any response, just return.
                    String output = "";
                    // things went well so let's read the response
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    while ((output = br.readLine()) != null) {
                        response += output;
                    }
                    conn.disconnect();
                    // return value from server
                    // set the response object
                    result.setValue(response);
                    // return HTTP status to caller
                    return status;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * To get the analysis result, please visit: https://dry-journey-68583.herokuapp.com/getAnalysis
         * this method sends three parameters amount, isChecked, type to the server.
         * Using http post to request. The server will grab the user data and post it to mongodb.
         * The client does not requires such request in Task 1.
         *
         * @param amount    is the usr input amount.
         * @param type      is the type of the school.
         * @return
         */
        public int sendReq(int amount, String type) {
            int status = 0;
            try {
                // Make call to a particular URL
                //task 1 path: https://salty-castle-05415.herokuapp.com/
                //default: https://glacial-gorge-63785.herokuapp.com/toAndroid
                URL url = new URL("https://glacial-gorge-63785.herokuapp.com/toAndroid");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // set request method to POST and send name value pair
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                // write to POST data area
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(amount + "," + type);
                out.flush();
                out.close();
                System.out.println(amount);
                // get HTTP response code sent by server
                status = conn.getResponseCode();
                //close the connection
                conn.disconnect();
            }
            // handle exceptions
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ConnectException e2) {
                e2.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // return HTTP status
            return status;
        }

        /**
         * call the read method which calls the getNews and returns a StringBuilder.
         *
         * @param urls
         * @return
         */
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder sbs = new StringBuilder();
            try {
                sbs = read(amount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sbs;
        }


        /**
         * once doInBackground finishes executing the task,
         * onPostExecute delivers the result back to the main UI thread and stops the AsyncTask process.
         *
         * @param st
         */
        protected void onPostExecute(StringBuilder st) {
            ip.newsReady(st);
        }

        /**
         * get the value after it receives it and split the mssg, and return a StringBuilder
         * this will allow the doInBackground to return the StringBuilder to the main thread.
         * the user deals with the cached data from the server
         * to improve efficiency without frequent requests.
         */
        public StringBuilder read(int amount) throws JSONException {
            Result r = new Result();
            StringBuilder sb = new StringBuilder();
            int status;
            if ((status = getNews(r)) != 200) {
                return sb.append("Error from server ").append(status);
            }
            sendReq(amount, type);
            //call getNews to send a request and set the value.
            getNews(r);
            System.out.println(r.getValue()); //get the value after it receives it and split the mssg.


            String[] results = r.getValue().split("@");//amount
            for (int i = 0; i < amount; i++) {
                sb = sb.append(i + 1).append(". ").append(results[i]).append("\n");
            }

            System.out.println(sb);
            return sb;
        }
    }
}

/**
 * It helps the getNews to set the value.
 * A wrapper class to wrap the result.
 */
class Result {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


//https://powerful-ridge-80098.herokuapp.com/
//http://10.0.2.2:8082/
//http://10.0.2.2:8083/Project4Task2_Server/Servlet/toAndroid