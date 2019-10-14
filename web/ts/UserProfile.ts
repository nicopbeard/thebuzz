/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
class UserProfile {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "UserProfile";

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
        if (!UserProfile.isInit) {
            UserProfile.isInit = true;
            $("#profile-container").append(Handlebars.templates[UserProfile.NAME + ".hb"]());
            $("#Profile-Back-Button").click(UserProfile.returnToMain);
        }
    }

        /**
     * refresh() is the public method for updating the ElementList
     */
    public static refresh(data: any) {
        // Make sure the singleton is initialized
        UserProfile.init();

        //AJAX CALL TO GET MY USERDATA
        // $.ajax({
        //     type: "GET",
        //     url: backendUrl + "/messages",
        //     dataType: "json",
        //     success: function(data) {
        //         UserProfile.update(data);
        //     }
        // });
 


        $("#" + UserProfile.NAME).remove();
        $("#profile-container").append(Handlebars.templates[UserProfile.NAME + ".hb"](data));
        $("#Profile-Back-Button").click(UserProfile.returnToMain);

    }

    public static hide() {
        $("#" + UserProfile.NAME).hide();
    }

    /**
     * update() is the private method used by refresh() to update the 
     * ElementList
     */
    private static update(data: any) {
        // Remove the table of data, if it exists
        $("#" + UserProfile.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("#profile-container").append(Handlebars.templates[UserProfile.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior

    }

    private static returnToMain() {
        UserProfile.hide();
        ElementList.show();
        NewEntryForm.show();
    }



}