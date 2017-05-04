<?php

class GCM {
 
    function __construct() {
         
    }
     function send_notification($registration_ids,$message) {

        // Set POST variables
        $url =  'https://fcm.googleapis.com/fcm/send';
 
        $fields = array(
            'registration_ids' => $registration_ids,
            'data' => $message
        );
  //echo json_encode($registration_ids);
        $headers = array(
            'Authorization: key=AAAAFMnovcI:APA91bEiG27OQgzlQ63iLP-Z3OAoON_CzoyEnmVSMZ4q8NaE3OhJSuCBcvaOISYw4Lq-y15MMiksNsHlzhqFykVmthB_IzbjjgyFJuE0mUmLKrW5s4NkwMaHzBh3EW_sSk_sbqnfiOPG',

            'Content-Type: application/json'
        );
        // Open connection
        $ch = curl_init();
 
        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);
 
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
 
        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
 
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 
        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        //echo $result;
        // Close connection
        curl_close($ch);
   
    }
}
 ?>
