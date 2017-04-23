<?php
$link = mysqli_connect("10.6.11.171", "root", "", "snu_library");
$rno=$_GET['rollnumber'];
	
$query = "UPDATE student_details set OutTime=NOW() where RollNumber='$rno';";


$query .="INSERT INTO history SELECT * from student_details where student_details.RollNumber='$rno';";


$query .="INSERT INTO tokens (token) SELECT (TokenNumber) from student_details where RollNumber='$rno';";


$query .="DELETE from student_details where student_details.RollNumber='$rno'";

if (mysqli_multi_query($link, $query)) {
do {
    /* store first result set */
    if ($result = mysqli_store_result($link)) {
        while ($row = mysqli_fetch_array($result)) 

mysqli_free_result($result);
}   
} while (mysqli_next_result($link));
}

?>