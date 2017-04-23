<?php
$link = mysqli_connect("10.6.11.171", "root", "", "snu_library");
		$rno=$_GET['rollnumber'];
		$name = $_GET['name'];
		$books = $_GET['books'];
		$token=$_GET['tokennumber'];		
		$b_details = $_GET['bookdetails'];
		
			require_once('dbConnect.php');

$sql = "SELECT * FROM student_details WHERE Name='$name'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			if(isset($check)){
				echo 'username or email already exist';
			}
else{

$query = "INSERT INTO student_details (RollNumber,Name,TokenNumber,InTime,Books,BooksDetails) VALUES('$rno','$name','$token',NOW(),'$books','$b_details');"; 


$query .= "UPDATE student_details set TokenNumber=(SELECT token FROM tokens ORDER BY rand() LIMIT 1) WHERE RollNumber='$rno';";

$query .= "DELETE from tokens WHERE token=(SELECT TokenNumber from student_details where RollNumber='$rno')";



if (mysqli_multi_query($link, $query)) {
do {
    /* store first result set */
    if ($result = mysqli_store_result($link)) {
        while ($row = mysqli_fetch_array($result)) 

mysqli_free_result($result);
}   
} while (mysqli_next_result($link));
}
}
?>