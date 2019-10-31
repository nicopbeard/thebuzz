<?php
      $googleClientID = '729841771303-h2118dlldms407v0jnbc7pfdadksf466.apps.googleusercontent.com';
      $googleClientSecret = 'AhuxzHdPho3QpAnsk55fRyHm';
      
      // This is the URL we'll send the user to first
      // to get their authorization
      $authorizeURL = 'https://accounts.google.com/o/oauth2/v2/auth';
      
      // This is Google's OpenID Connect token endpoint
      $tokenURL = 'https://www.googleapis.com/oauth2/v4/token';
      
      // The URL for this script, used as the redirect URL
      $baseURL = 'https://' . $_SERVER['SERVER_NAME']
          . $_SERVER['PHP_SELF'];
      
      // Start a session so we have a place
      // to store things between redirects
      session_start();

      // If there is a user ID in the session
      // the user is already logged in
      if(!isset($_GET['action'])) {
        if(!empty($_SESSION['user_id'])) {
          echo '<h3>Logged In</h3>';
          echo '<p>User ID: '.$_SESSION['user_id'].'</p>';
          echo '<p>Email: '.$_SESSION['email'].'</p>';
          echo '<p><a href="?action=logout">Log Out</a></p>';
       
          // Fetch user info from Google's userinfo endpoint
          echo '<h3>User Info</h3>';
          echo '<pre>';
          $ch = curl_init('https://www.googleapis.com/oauth2/v3/userinfo');
          curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Authorization: Bearer '.$_SESSION['access_token']
          ]);
          curl_exec($ch);
          echo '</pre>';
       
        } else {
          echo '<h3>Not logged in</h3>';
          echo '<p><a href="?action=login">Log In</a></p>';
        }
        die();
      }      
      
      // Start the login process by sending the user
      // to Google's authorization page
      if(isset($_GET['action']) && $_GET['action'] == 'login') {
        unset($_SESSION['user_id']);
       
        // Generate a random hash and store in the session
        $_SESSION['state'] = bin2hex(random_bytes(16));
       
        $params = array(
          'response_type' => 'code',
          'client_id' => $googleClientID,
          'redirect_uri' => $baseURL,
          'scope' => 'openid email',
          'state' => $_SESSION['state']
        );
       
        // Redirect the user to Google's authorization page
        header('Location: '.$authorizeURL.'?'.http_build_query($params));
        die();
      }
      if(isset($_GET['action']) && $_GET['action'] == 'logout') {
        unset($_SESSION['access_token']);
        header('Location: '.$baseURL);
        die();
      }
      // When Github redirects the user back here,
      // there will be a "code" and "state" parameter in the query string
      if(isset($_GET['code'])) {
        // Verify the state matches our stored state
        if(!isset($_GET['state'])
          || $_SESSION['state'] != $_GET['state']) {
          header('Location: ' . $baseURL . '?error=invalid_state');
          die();
        }
        // Exchange the auth code for an access token
        $token = apiRequest($tokenURL, array(
          'grant_type' => 'authorization_code',
          'client_id' => $githubClientID,
          'client_secret' => $githubClientSecret,
          'redirect_uri' => $baseURL,
          'code' => $_GET['code']
        ));
        $_SESSION['access_token'] = $token['access_token'];
        header('Location: ' . $baseURL);
        die();
      }
      if(isset($_GET['action']) && $_GET['action'] == 'repos') {
        // Find all repos created by the authenticated user
        $repos = apiRequest($apiURLBase.'user/repos?'.http_build_query([
          'sort' => 'created',
          'direction' => 'desc'
        ]));
        echo '<ul>';
        foreach($repos as $repo) {
          echo '<li><a href="' . $repo['html_url'] . '">'
            . $repo['name'] . '</a></li>';
        }
        echo '</ul>';
      }
      
      // If there is an access token in the session
      // the user is already logged in
      if(!isset($_GET['action'])) {
        if(!empty($_SESSION['access_token'])) {
          echo '<h3>Logged In</h3>';
          echo '<p><a href="?action=repos">View Repos</a></p>';
          echo '<p><a href="?action=logout">Log Out</a></p>';
        } else {
          echo '<h3>Not logged in</h3>';
          echo '<p><a href="?action=login">Log In</a></p>';
        }
        die();
      }
      // This helper function will make API requests to GitHub, setting
      // the appropriate headers GitHub expects, and decoding the JSON response
      function apiRequest($url, $post=FALSE, $headers=array()) {
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        if($post)
          curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($post));
        $headers = [
          'Accept: application/vnd.github.v3+json, application/json',
          'User-Agent: https://example-app.com/'
        ];
        if(isset($_SESSION['access_token']))
          $headers[] = 'Authorization: Bearer ' . $_SESSION['access_token'];
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        $response = curl_exec($ch);
        return json_decode($response, true);
      }      

?>