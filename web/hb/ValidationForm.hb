<div id="ValidationForm">  

    <script>
        public static onSignIn(googleUser) {
        // Useful data for your client-side scripts:
        console.log(googleUser);
        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
    </script>
    <div id="ValidationForm-google" class="g-signin2" data-onsuccess="onSignIn" data-onfailure="onFailure"" 
    data-redirecturi="https://clowns-who-code.herokuapp.com"
    data-theme="dark"></div>



    <div class="form-group">
        <label for="loginUsername">Username</label>
        <input type="username" class="form-control" id="ValidationForm-username" aria-describedby="emailHelp" placeholder="Enter username">
    </div>
    <div class="form-group">
        <label for="loginPassword">Password</label>
        <input type="password" class="form-control" id="ValidationForm-password" placeholder="Password">
    </div>
    <div class="form-group">
            <label for="loginPassword">Email Address (Only for registration)</label>
            <input type="text" class="form-control" id="ValidationForm-name" placeholder="Email">
        </div>

    <button type="button" class="btn btn-default" id="ValidationForm-Register">Register</button>
    <button type="button" class="btn btn-default" id="ValidationForm-Login">Login</button>

</div>