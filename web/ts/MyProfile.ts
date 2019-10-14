/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
class MyProfile {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "MyProfile";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the ElementList singleton.  
     * This needs to be called from any public static method, to ensure that the 
     * Singleton is initialized before use.
     */
    private static init() {
        if (!MyProfile.isInit) {
            MyProfile.isInit = true;
            $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"]());
            $("#Profile-Back-Button").click(MyProfile.returnToMain);
        }
    }

        /**
     * refresh() is the public method for updating the ElementList
     */
    public static refresh() {
        // Make sure the singleton is initialized
        MyProfile.init();

        //AJAX CALL TO GET MY USERDATA
        // $.ajax({
        //     type: "GET",
        //     url: backendUrl + "/messages",
        //     dataType: "json",
        //     success: function(data) {
        //         MyProfile.update(data);
        //     }
        // });
        let data = {
            username: "Test_Username",
            email: "Test_Email", 
            comment: "Test Comment"
        }
        $("#" + MyProfile.NAME).remove();
        $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"](data));
        $("#Profile-Back-Button").click(MyProfile.returnToMain);

    }

    public static hide() {
        $("#" + MyProfile.NAME).hide();
    }

    /**
     * update() is the private method used by refresh() to update the 
     * ElementList
     */
    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + MyProfile.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior

    }

    private static returnToMain() {
        MyProfile.hide();
        ElementList.show();
        NewEntryForm.show();
    }



}