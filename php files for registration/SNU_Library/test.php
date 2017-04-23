<?php
$link = mysqli_connect("10.6.11.171", "root", "", "snu_library");

$result = mysqli_query('snu_library',"SELECT TokenNumber FROM student_details WHERE RollNumber='[1510110314]'") or die(mysqli_error());
while($row = mysqli_fetch_assoc($result)){
      echo $row['TokenNumber'];
}

?>