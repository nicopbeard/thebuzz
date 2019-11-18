/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
class ElementList {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "ElementList";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;
    private static map = {};
    /**
     * Initialize the ElementList singleton.  
     * This needs to be called from any public static method, to ensure that the 
     * Singleton is initialized before use.
     */
    private static init() {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    }

        /**
     * refresh() is the public method for updating the ElementList
     */
    public static refresh() {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages",
            dataType: "json",
            success: function(data) {
                ElementList.update(data);
            }
        });
    }

    public static hide() {
        $("#message-container").hide();
    }

    public static show() {
        $("#message-container").show();
    }


    /**
     * update() is the private method used by refresh() to update the 
     * ElementList
     */
    private static update(data: any) {
        // Remove the table of data, if it exists
        console.log("Data is");
        console.log(data);


        for(let i = 0; i < data.mData.length; i++){
            let text = data.mData[i].text;
            ElementList.map[27] = data.mData[i];
            let number = -1;
            for(let j = 0; j < text.length; j++){
                data.mData[i].linkMsg = number;
                if(text[j] == '/'){
                    console.log("Found forward slash");
                    number = 0;
                    for(let k = j + 1; k < text.length; k++){
                        if(text[k] >= '0' && text[k] <= '9') {
                            number *= 10;
                            number += parseInt(text[k]);
                            console.log("number is: "  + number)
                        } else {
                            break;
                        }
                    }
                    data.mData[i].linkMsg = number;
                    console.log("Found hashtag -- number is " + number);
                }
            }
        }

        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("#message-container").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior
        $("." + ElementList.NAME + "-upvotebtn").click(ElementList.clickUpVote);
        // Find all of the Downvote buttons, and set their behavior
        $("." + ElementList.NAME + "-downvotebtn").click(ElementList.clickDownVote);
        $("." + ElementList.NAME + "-linkMsgButton").click(ElementList.clickMsgLink);
        $("." + ElementList.NAME + "-fileButton").click(ElementList.clickFile);

    }

    /**
     * buttons() creates 'edit' and 'delete' buttons for an id, and puts them in
     * a TD
     */
    private static buttons(id: string): string {
        return "<td><button class='" + ElementList.NAME +
            "-upvotebtn' data-value='" + id + "'>Like</button></td>" +
            "<td><button class='" + ElementList.NAME +
            "-downvotebtn' data-value='" + id + "'>Dislike</button></td>";
    }

    /**
     * clickUpvote is the code we run in response to a click of a upvote button
    */

    private static clickUpVote() {
        let msgId = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/like",
            dataType: "json",
            data: JSON.stringify({ 
                                    userId: ID, 
                                    msgId: msgId,}
                                ),
            success: ElementList.refresh,
            error: function(e){
                console.info(e);
            }
        });
    }

    /**
     * clickDownVote is the code we run in response to a click of a downvote button
    */
    private static async clickDownVote()
    {
        let msgId = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/dislike",
            dataType: "json",
            data: JSON.stringify({ 
                                    userId: ID, 
                                    msgId: msgId,}
                                ),
            success: ElementList.refresh,
            error: function(e){
                console.info(e);
            }
        });
    }

    private static clickMsgLink() {
        console.log("Length of map is: " + Object.keys(ElementList.map).length);

        let msgId = $(this).data("value");
        console.log(JSON.stringify(ElementList.map[msgId]));
        if(ElementList.map[msgId] == null){
            window.alert("No message linked in this message.");
        } else {
            window.alert(JSON.stringify(ElementList.map[msgId]));
        }

    }

    private static clickFile() {
       console.log("in click file");
    }
}