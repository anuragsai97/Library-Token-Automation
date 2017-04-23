<?php 

	if($_SERVER['REQUEST_METHOD']=='GET'){
		
		$id  = $_GET['id'];
		//$id='[1510110314]';
		
		require_once('dbConnect.php');
		
		$sql = "SELECT TokenNumber FROM student_details WHERE RollNumber='".$id."'";
		
		$r = mysqli_query($con,$sql);
		
		$res = mysqli_fetch_array($r);
		
		$result = array();
		
		array_push($result,array(
			"token"=>$res['TokenNumber']			)
		);
		
		echo json_encode(array("result"=>$result));
		
		mysqli_close($con);
		
	}