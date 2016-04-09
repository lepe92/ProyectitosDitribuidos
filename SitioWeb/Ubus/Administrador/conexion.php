<?php
if(!isset($_SESSION)){
	session_start();
//	echo "Inicializado";
	}
$hostname_conexion="mysql6.000webhost.com";
$database_conexion="a2811468_Transpo";
$username_conexion="a2811468_Transpo";
$password_conexion="comp15";
$conexion = mysqli_connect($hostname_conexion, $username_conexion, $password_conexion) or trigger_error(mysql_error(),E_USER_ERROR); 
//echo "Conectado correctamente";
//include('js/inc/funciones.php');
?>