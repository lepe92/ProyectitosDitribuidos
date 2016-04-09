<?php

//foreach ($_POST as $key => $value) {
    //do something
  //  echo $key . ' has the value of ' . $value;
//}
$u= $_POST['usuario'];
$c =$_POST['correo'];
//echo $u;
//echo $c;

$servername = "mysql6.000webhost.com";
    $username = "a2811468_Transpo";
    $password = "comp15";
    $database = "a2811468_Transpo";
     
    $enlace =  mysqli_connect($servername, $username, $password,$database);
    if (!$enlace) {
        die('No pudo conectarse: ' . mysql_error());
    }
    else{
       $res2 = $enlace->query("INSERT INTO Usuario VALUES('','".$u."','".$c."')");
if($res2){
echo "insertado";
}
else {echo "algo ha fallado";}
    }#end else
    mysqli_close($enlace);
?>