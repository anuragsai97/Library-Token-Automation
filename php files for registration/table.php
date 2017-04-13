<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "registration";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// sql to create table
$sql = "CREATE TABLE `users` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1";

if (mysqli_query($conn, $sql)) {
    echo "Table users created successfully";
} else {
    echo "Error creating table: " . mysqli_error($conn);
}

mysqli_close($conn);
?>














CREATE TABLE `users` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1