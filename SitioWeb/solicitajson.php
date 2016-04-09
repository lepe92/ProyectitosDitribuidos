<?php
define('HOST','mysql6.000webhost.com');
define('USER','a2811468_root');
define('PASS','ornitorrinco8');
define('DB','a2811468_distrib');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select id, nombre from personas";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('id'=>$row[0],
'nombre'=>$row[1],
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
?>