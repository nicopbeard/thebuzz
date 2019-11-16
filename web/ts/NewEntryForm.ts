/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {

    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    private static readonly NAME = "NewEntryForm";

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
        if (!NewEntryForm.isInit) {
            $("#input-container").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
            $("#" + NewEntryForm.NAME + "-CLEAR").click(NewEntryForm.clear);
            NewEntryForm.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        NewEntryForm.init();
    }

    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    public static hide() {
       $("#input-container").hide();
    }
    public static show() {
        $("#input-container").show();
     }

     private static clear() {
         $("#NewEntryForm-message").val("");
     }




    /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        let id = $(this).data("value");
        
        
        //formData.append('senderId', id);
        //formData.append('text', msg);
        //formData.append('nUpVotes', '0');
        //formData.append('nDownVotes', '0');

        if (msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        
        $("#NewEntryForm-message").val("");
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        var fileData = null;
        if((<HTMLInputElement>document.getElementById('docpicker')).files[0] != null){
            var reader = new FileReader();
            reader.readAsDataURL((<HTMLInputElement>document.getElementById('docpicker')).files[0]);
            reader.onload = function() {

                console.log("The data in the file is:");
                console.log(reader.result);

                $.ajax({
                    type: "POST",
                    url: backendUrl + "/messages",
                    dataType: "json",
                    data: JSON.stringify({ 
                        senderId: id, 
                        text: msg,
                        nUpVotes: 0,
                        nDownVotes: 0,
                        fileName: 'fileName',
                        file: reader.result
                    }),
                    processData: false,
                    success: NewEntryForm.onSubmitResponse
                });

                (<HTMLInputElement>document.getElementById('docpicker')).value = '';
            }
        } else {

            $.ajax({
                type: "POST",
                url: backendUrl + "/messages",
                dataType: "json",
                data: JSON.stringify({ 
                    senderId: id, 
                    text: msg,
                    nUpVotes: 0,
                    nDownVotes: 0,
                    fileName: 'fileName',
                    file: 'null'
                }),
                processData: false,
                success: NewEntryForm.onSubmitResponse
            });
        }
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