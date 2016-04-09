<?php
require_once('conexion.php');//llama al php para realizar la conexion con la base de datos

?>
<!doctype html>
<html>
<head
<meta charssdet="iso-8859-1">

<title>Untitled Document</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1" name='viewport'>
<link rel="stylesheet" type="text/css" href="css/estilo.css">
<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script src="js/efecto.js"></script>
</head>

<body>

<header class="encabezado">

<div id="logo"><img src="img/Cinvestav.png" width="80" height="80" alt=""/></div>

</header>

<nav class="menu">

<a href="http://jimenezlepe.comuv.com/Ubus/Administrador/Rutas/Rutas.php"; class="cursor"><li class="item_l">Rutas</li></a>
<a href="Contacto.php"; class="cursor"><li class="item_l">Contacto</li></a>



<?php if(!isset($_SESSION['iduser'])){?>

<!--<a onClick="ventanas(1);" class="cursor"><li class="item_l">Iniciar Sesion</li></a>-->

<?php	} else if(isset($_SESSION['iduser'])){ echo "Adentro";?>
	
	<li class="item_l"><?php echo $_SESSION['nombreuser']?></li> 
<?php } ?>
	
	
	
</nav>
<nav class=cuerpo2>
<section id="banner">
<div class="inner">
	<h2>This is Ubus</h2>
	<p>Everyone's Driver	</p>
	<ul class="actions">
	
	</ul>
	</div>
</section>
</nav>
<div id="cogebox" style="display:none">
<div id="flotantelogin">

<a onClick="ventanas(2);" class="cursor"><span class="derecha">x</span></a>



<form onSubmit="return false" id="formulatioLogin"></form>
Usuario<br/>
<input type="text" name:"user" id="user"><br/>
Contraseña<br/>
<input type="password" name:"pass" id="pass"><br/>
<input type="submit" id="miboton" value="Iniciar" class="cursor" onClick="login_ajax(user.value, pass.value);"><br/>

</div>

<div id="fondoNegro"></div>
</div>
</body>
</html>