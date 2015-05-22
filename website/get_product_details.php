<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title></title>
    </head>
    <body>




<?php
 
/*
 * Following code will get single leraar details
 * A leraar is identified by leraar id (ID)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
$validInput = 0;

// check for post data
if (isset($_GET["submit"])) {
    
    $validInput = 2;
    $fullStr = $_GET["ID"];

    $arrayParts = explode(", ", $fullStr);
    
    print_r($arrayParts);
    echo "<br> <br>";
    
    if(count($arrayParts)> 1){
    $table = array_values($arrayParts)[0];

    $idNumber = array_values($arrayParts)[1];
    
     if($table == "leraar"){
    
         $validInput = 1;
         
    // get a leraar from leraar table
    $result = mysql_query('SELECT * FROM leraar WHERE ID = '. $idNumber .' ');
 
    if (!empty($result)) {
        
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $leraar = array();
            $leraar["ID"] = $result["ID"];
            $leraar["Naam"] = $result["Naam"];
//            $leraar["price"] = $result["price"];
//            $leraar["description"] = $result["description"];
//            $leraar["created_at"] = $result["created_at"];
//            $leraar["updated_at"] = $result["updated_at"];
//            // success
            $response["success"] = 1;
 
            // user node
            $response["leraar"] = array();
 
            array_push($response["leraar"], $leraar);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no leraar found
            $response["success"] = 0;
            $response["message"] = "No leraar found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no leraar found
    
        $response["success"] = 0;
        $response["message"] = "No leraar found2";
        
 
        // echo no users JSON
        echo json_encode($response);
    }
     }
     
          if($table == "lokaal"){
              
              $validInput = 1;
    
    // get a lokaal from lokaal table
    $result = mysql_query('SELECT * FROM lokaal WHERE ID = '. $idNumber .' ');
 
    if (!empty($result)) {
        
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $lokaal = array();
            $lokaal["ID"] = $result["ID"];
            $lokaal["Nummer"] = $result["Nummer"];
            $lokaal["Functie"] = $result["Functie"];
//            $lokaal["description"] = $result["description"];
//            $lokaal["created_at"] = $result["created_at"];
//            $lokaal["updated_at"] = $result["updated_at"];
//            // success
            $response["success"] = 1;
 
            // user node
            $response["lokaal"] = array();
 
            array_push($response["lokaal"], $lokaal);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no lokaal found
            $response["success"] = 0;
            $response["message"] = "No lokaal found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no lokaal found
    
        $response["success"] = 0;
        $response["message"] = "No lokaal found2";
        
 
        // echo no users JSON
        echo json_encode($response);
    }
    
    
    
    
    
    
    
    
    
    } }
    elseif($validInput != 1) {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing or wrong input";
 
    // echoing JSON response
    echo json_encode($response);
    }
    elseif ($validInput == 2) {
        // submit failed
    $response["success"] = 0;
    $response["message"] = "Submit failed! try again.";
 
    // echoing JSON response
    echo json_encode($response);
}



}
?>







        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="get">
            <p>Add Tabel & ID separated by comma</p>
            <p>QR: <input type="text" name="ID"
                                 /></p>
               
            <p><input type="submit" name="submit"
                      value="Submit" />
        
        </form>
        
    </body>
</html>
