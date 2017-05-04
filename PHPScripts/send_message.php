<?php
session_start();
$response = array();
$city=$_POST['city'];
$id=$_POST['id'];

include('dbconfig.php');
include('fcm.php');


$fcm=new GCM();
$tokens=array();
$message="Lets do a good Deed by Donating Blood";
$noti = array("message" => $message);
$_SESSION['id'] = $id;

$resNoti=@mysql_query("SELECT token FROM UserDetails where city='$city'");
if(mysql_num_rows($resNoti)>0){
	while ($row=@mysql_fetch_array($resNoti)) {
		$dev_id=$row['token'];
		array_push($tokens, $dev_id);
	}
	$fcm->send_notification($tokens,$noti);
	$response["iddd"] = $_SESSION['id'];	
	$response["success"] = 1;
	$response["message"] = "Notification Sent";
}else{
	$response["success"] = 0;
	$response["message"] = "Notification Not Sent";
}

echo json_encode($response);
?>					 