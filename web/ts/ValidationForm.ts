/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class ValidationForm {

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
            ValidationForm.isInit = true;
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
        let name = $('#' + ValidationForm.NAME + "-name").val();

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
        if(sucess) {
            ValidationForm.hide();
            ElementList.refresh();
            NewEntryForm.refresh();
            Navbar.welcomeUser("Kevin");
            console.log(`Sucessfully Verified ${data.userid} in...\n Current Session Token: ${data.session_token}}`);
        }


    }
}