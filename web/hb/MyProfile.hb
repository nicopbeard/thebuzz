<div class = "container" id = "MyProfile">
    <button type="button" class="btn btn-default" id="Profile-Back-Button">Back</button>

    <div class = "row" id = "username-row"> 
        <div class = "col-2">  Username:  </div>
        <div class = "col-2"> {{this.username}} </div>
        <div class = "col-8"> </div>
    </div>

    <div class = "row" id = "email-row"> 
            <div class = "col-2">Email:</div>
            <div class = "col-2"> {{this.email}} </div>
            <div class = "col-8"> </div>
    </div>

    <div class = "row" id = "comment-row"> 
            <div class = "col-2">Comment:</div>
            <div class = "col-2"> <input id="comment-field" value = "{{this.comment}}"> </input> </div>
            <div class = "col-2"> <button> Change Comment </button> </div>
            <div class = "col-6"> </div>
    </div>

    <div class = "row" id = "password-row"> 
            <div class = "col-2">Password:</div>
            <div class = "col-2"> 
                <input id="password-field" placeholder = "Enter new password"> </input>
                <input id="password-confirm-field" placeholder = "Confirm Password"> </input>
            
            </div>
            <div class = "col-2"> <button> Change Password </button> </div>
            <div class = "col-6"> </div>
    </div>

</div>
