<?php
header('Content-Type: application/json; charset=utf-8');

function Conectar($host,$user,$pass,$dbname)
{
	
   $conexion=mysqli_connect($host,$user,$pass) or die("ERROR CONEXION");
   mysqli_set_charset($conexion,'utf-8');
   mysqli_select_db($conexion,$dbname) or die("ERROR CONEXION");
   return $conexion;
}

function TABLA_A_JSON($resultado)
{
   $respuesta=array();
   while($fila=mysqli_fetch_assoc($resultado))
   {
         array_push($respuesta, $fila);
   }
   return json_encode($respuesta);
}

$host='localhost';
$user='root';
$pass='';
$dbname='scoreboards';

$conexion=Conectar($host,$user,$pass,$dbname);

   $opcion=$_GET["accion"];
   switch($opcion)
   {
    	case "consulta":
          $query="SELECT idLogro,nombre,apiname,imagen,imagengrey FROM logro WHERE idJuego = 730";
          $resultado=mysqli_query($conexion,$query) or die("ERROR");
          $json=TABLA_A_JSON($resultado);
          echo $json;
    	break;
		
		case "loginusuario":
          $query="SELECT * FROM usuario";
          $resultado=mysqli_query($conexion,$query) or die("ERROR");
          $json=TABLA_A_JSON($resultado);
          echo $json;
    	break;
		
		case "selectuser":
		  $user=$_GET["user"];
          $query="SELECT * FROM usuario WHERE idUsuario = '$user'";
          $resultado=mysqli_query($conexion,$query) or die("ERROR");
          $json=TABLA_A_JSON($resultado);
          echo $json;
    	break;
		
		case "selectjuegoscompradospor":
		  $user=$_GET["user"];
          $query="SELECT * FROM juego WHERE idJuego IN(SELECT idJuego FROM posee WHERE idUsuario = '$user')";
          $resultado=mysqli_query($conexion,$query) or die("ERROR");
          $json=TABLA_A_JSON($resultado);
          echo $json;
    	break;
		
		case "listarlogros":
		  $juego=$_GET["juego"];
          $query="SELECT idLogro,nombre,apiname,descripcion,imagen,imagengrey FROM logro WHERE idJuego = '$juego'";
          $resultado=mysqli_query($conexion,$query) or die("ERROR");
          $json=TABLA_A_JSON($resultado);
          echo $json;
    	break;
      
      case "insertar":
           $user=$_GET["user"];
           $pass=$_GET["pass"];
           $query="INSERT INTO usuario (user,pass,tipo) values ('$user','$pass','normal');";
           mysqli_query($conexion,$query) or die("ERROR");
           echo '{"estado":true}';
      break;

      case "borrar":
           $user=$_GET["user"];
           $query="DELETE FROM usuario WHERE user='$user';";
           mysqli_query($conexion,$query) or die("ERROR");
           echo '{"estado":true}';
      break;
      
      case "actualizar":
           $user=$_GET["user"];
           $pass=$_GET["pass"];
           $query="UPDATE usuario SET pass='$pass' WHERE user='$user';";
           mysqli_query($conexion,$query) or die("ERROR");
           echo '{"estado":true}';
      break;
    }




?>

