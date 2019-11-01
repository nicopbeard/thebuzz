/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class Login {

    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    private static readonly NAME = "Login";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;
    static templates: any;

    /**
     * Initialize the NewEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!Login.isInit) {
            $("#input-container").append(Login.templates[Login.NAME + ".hb"]());
            $("#" + Login.NAME + "-OK").click(Login.submitForm);
            $("#" + Login.NAME + "-Close").click(Login.hide);
            Login.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        Login.init();
    }

    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    private static hide() {
        $("#" + Login.NAME + "-title").val("");
        $("#" + Login.NAME + "-message").val("");
        $("#" + Login.NAME).modal("hide");
    }

    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        $("#" + Login.NAME + "-title").val("");
        $("#" + Login.NAME + "-message").val("");
        $("#" + Login.NAME).modal("show");
    }


    /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let msg = "" + $("#" + Login.NAME + "-message").val();
        let id = $(this).data("value");
        if (msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        Login.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: backendUrl + "/messages",
            dataType: "json",
            data: JSON.stringify({ 
                                    senderId: id, 
                                    text: msg, 
                                    nUpVotes: 0,
                                    nDownVotes: 0}
                                ),
            success: Login.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            ElementList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
}