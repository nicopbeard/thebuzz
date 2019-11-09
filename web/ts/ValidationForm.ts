

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */

class ValidationForm {

<<<<<<< HEAD
  /**
   * The name of the DOM entry associated with NewEntryForm
   */
  private static readonly NAME = "ValidationForm";

  /**
   * Track if the Singleton has been initialized
   */
  private static isInit = false;

  /**
   * Initialize the NewEntryForm by creating its element in the DOM and 
   * configuring its buttons.  This needs to be called from any public static 
   * method, to ensure that the Singleton is initialized before use
   */
  private static init() {
    if (!ValidationForm.isInit) {
      $("#login-container").append(Handlebars.templates[ValidationForm.NAME + ".hb"]());
      $("#" + ValidationForm.NAME + "-Register").click(ValidationForm.register);
      $("#" + ValidationForm.NAME + "-Login").click(ValidationForm.login);
      //$("#" + ValidationForm.NAME + "-google").success(ValidationForm.onSignIn);

      ValidationForm.isInit = true;
=======
    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    private static readonly NAME = "ValidationForm";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the NewEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {

        if (!ValidationForm.isInit) {
            //oauthSignIn();
            $("#login-container").append(Handlebars.templates[ValidationForm.NAME + ".hb"]());
            $("#" + ValidationForm.NAME + "-Register").click(ValidationForm.register);
            $("#" + ValidationForm.NAME + "-Login").click(ValidationForm.login);
            ValidationForm.isInit = true;
        }
>>>>>>> a42c41b5da28f861a20f5249c8d7cb8d0f690e32
    }
  }

  /**
   * Refresh() doesn't really have much meaning, but just like in sNavbar, we
   * have a refresh() method so that we don't have front-end code calling
   * init().
   */
  public static refresh() {
    ValidationForm.init();
  }

  /**
   * Hide the NewEntryForm.  Be sure to clear its fields first
   */
  private static hide() {
    $("#" + ValidationForm.NAME + "-username").val("");
    $("#" + ValidationForm.NAME + "-password").val("");
    $("#" + ValidationForm.NAME).hide();
  }

  /**
   * Show the NewEntryForm.  Be sure to clear its fields, because there are
   * ways of making a Bootstrap modal disapper without clicking Close, and
   * we haven't set up the hooks to clear the fields on the events associated
   * with those ways of making the modal disappear.
   */
  public static show() {
    $("#" + ValidationForm.NAME + "-username").val("");
    $("#" + ValidationForm.NAME + "-password").val("");
    $("#" + ValidationForm.NAME).show();
  }


  /**
   * Send data to submit the form only if the fields are both valid.  
   * Immediately hide the form when we send data, so that the user knows that 
   * their click was received.
   */
  private static register() {
    // get the values of the two fields, force them to be strings, and check 
    // that neither is empty
    let username = $("#" + ValidationForm.NAME + "-username").val();
    let password = $('#' + ValidationForm.NAME + "-password").val();

    if (username === "" || password === "") {
      window.alert("Error: Username or Password field incomplete");
      return;
    }
    $("#" + ValidationForm.NAME + "-username").val("");
    $("#" + ValidationForm.NAME + "-password").val("");
    $("#" + ValidationForm.NAME + "-name").val("");

    //ValidationForm.hide();
    // Unknown call here, have to wait for backend 
    // $.ajax({
    //     type: "POST",
    //     url: backendUrl + "/messages",
    //     dataType: "json",
    //     data: JSON.stringify({ 
    //                             senderId: id, 
    //                             text: msg, 
    //                             nUpVotes: 0,
    //                             nDownVotes: 0}
    //                         ),
    //     success: ValidationForm.onSubmitResponse
    // });
    let data = {
      userid: "Test_Register123",
      session_token: "abc123zyz"
    }

    ValidationForm.onSubmitResponse(data);
  }

  private static login() {
    // get the values of the two fields, force them to be strings, and check 
    // that neither is empty
    let username = $("#" + ValidationForm.NAME + "-username").val();
    let password = $('#' + ValidationForm.NAME + "-password").val();
    if (username === "" || password === "") {
      window.alert("Error: Username or Password field incomplete");
      return;
    }
    $("#" + ValidationForm.NAME + "-username").val("");
    $("#" + ValidationForm.NAME + "-password").val("");
    //ValidationForm.hide();
    // Unknown call here, have to wait for backend 
    // $.ajax({
    //     type: "POST",
    //     url: backendUrl + "/messages",
    //     dataType: "json",
    //     data: JSON.stringify({ 
    //                             senderId: id, 
    //                             text: msg, 
    //                             nUpVotes: 0,
    //                             nDownVotes: 0}
    //                         ),
    //     success: ValidationForm.onSubmitResponse
    // });
    let data = {
      userid: "Test_Login123",
      session_token: "abc123zyz"
    }

    ValidationForm.onSubmitResponse(data);
  }

  /**
   * onSubmitResponse runs when the AJAX call in submitForm() returns a 
   * result.
   * 
   * @param data The object returned by the server
   */
  private static onSubmitResponse(data: any) {
    let sucess = true;
    // If we get an "ok" message, clear the form and refresh the main 
    // listing of messages
    // if (data.mStatus === "ok") {
    //     ElementList.refresh();
    // }
    // // Handle explicit errors with a detailed popup message
    // else if (data.mStatus === "error") {
    //     window.alert("The server replied with an error:\n" + data.mMessage);
    // }
    // // Handle other errors with a less-detailed popup message
    // else {
    //     window.alert("Unspecified error");
    // }



    if (sucess) {
      ValidationForm.hide();
      ElementList.refresh();
      NewEntryForm.refresh();
      Navbar.welcomeUser("Nicoooo");
      console.log(`Sucessfully Verified ${data.userid} in...\n Current Session Token: ${data.session_token}`);
    }


  }


  //placeholder for google login functions until i figure out wtf is going on

  // If there's an access token, try an API request.
  // Otherwise, start OAuth 2.0 flow.
  public static trySampleRequest() {
    console.log("Hello");
    var params = JSON.parse(localStorage.getItem('oauth2-test-params'));
    if (params && params['access_token']) {
      var xhr = new XMLHttpRequest();
      xhr.open('GET',
        'https://www.googleapis.com/drive/v3/about?fields=user&' +
        'access_token=' + params['access_token']);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
          console.log(xhr.response);
        } else if (xhr.readyState === 4 && xhr.status === 401) {
          // Token invalid, so prompt for user permission.
          oauth2SignIn();
        }
      };
      xhr.send(null);
    } else {
      oauth2SignIn();
    }
  }

  /*
   * Create form to request access token from Google's OAuth 2.0 server.
   */
  public static oauth2SignIn() {
    // Google's OAuth 2.0 endpoint for requesting an access token
    var oauth2Endpoint = 'https://accounts.google.com/o/oauth2/v2/auth';

    // Create element to open OAuth 2.0 endpoint in new window.
    var form = document.createElement('form');
    form.setAttribute('method', 'GET'); // Send as a GET request.
    form.setAttribute('action', oauth2Endpoint);

    // Parameters to pass to OAuth 2.0 endpoint.
    var params = {
      'client_id': '755594120508-vajdss1hrkqi335sukhur5qaa70s5jji.apps.googleusercontent.com',
      'redirect_uri': 'https://clowns-who-code.herokuapp.com',
      'scope': 'https://www.googleapis.com/auth/drive.metadata.readonly',
      'state': 'try_sample_request',
      'include_granted_scopes': 'true',
      'response_type': 'token'
    };

    // Add form parameters as hidden input values.
    for (var p in params) {
      var input = document.createElement('input');
      input.setAttribute('type', 'hidden');
      input.setAttribute('name', p);
      input.setAttribute('value', params[p]);
      form.appendChild(input);
    }

    // Add form to page and submit it to open the OAuth 2.0 endpoint.
    document.body.appendChild(form);
    form.submit();
  }

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
  }

  public static onFailure() { console.error('Sign in has failed!'); }
}