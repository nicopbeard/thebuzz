<div class="panel panel-default" id="ElementList">
    <div class="panel-heading">
        <h3 class="panel-title">Messages</h3>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td>{{this.mSubject}}</td>
                <td>{{this.mMessage}}</td>
                <td><button type="button" class="ElementList-upvotebtn btn-primary" data-value="{{this.mId}}">
                    <span class="glyphicon glyphicon-thumbs-up"></span>
                    Like</button></td>
                <td><button class="ElementList-downvotebtn btn-danger" data-value="{{this.mId}}">
                    <span class="glyphicon glyphicon-thumbs-down"></span>
                    Dislike</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>