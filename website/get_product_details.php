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
 
// check for post data
if (isset($_GET["submit"])) {
    $ID = $_GET["ID"];

 
    // get a leraar from leraar table
    $result = mysql_query('SELECT * FROM leraar WHERE ID = '. $ID .' ');
 
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
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>







        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="get">
            <p>Add Name and E-mail</p>
            <p>ID: <input type=number name="ID"
                                 /></p>
               
            <p><input type="submit" name="submit"
                      value="Submit" />
        
        </form>
        
    </body>
</html>
