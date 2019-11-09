<div class="panel panel-default" id="ElementList">
    <div class="panel-heading">
        <h3 class="panel-title"></h3>
    </div>
    <table class="table" id = message-table>
        <tbody>
            {{#each mData}}
            <tr>
                <td>{{this.senderId}}</td>
                <td>{{this.text}}</td>
                <td><button type="button" class="ElementList-upvotebtn btn-primary" data-value="{{this.id}}">
                    <span class="glyphicon glyphicon-thumbs-up"></span>
                    Like</button></td>
                <td><button class="ElementList-downvotebtn btn-danger" data-value="{{this.id}}">
                    <span class="glyphicon glyphicon-thumbs-down"></span>
                    Dislike</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div>