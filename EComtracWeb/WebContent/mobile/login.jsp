<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>EzBiz</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimal-ui" />
    <meta name="apple-mobile-web-app-status-bar-style" content="yes" />
    <link rel="shortcut icon" href="/favicon.png" type="image/x-icon" />
    <link rel="stylesheet" href="css/mobile-angular-ui-hover.min.css" />
    <link rel="stylesheet" href="css/mobile-angular-ui-base.min.css" />
    <link rel="stylesheet" href="css/mobile-angular-ui-desktop.min.css" />
    <link rel="stylesheet" href="demo.css" />
    <script src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.0/angular.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.0/angular-route.min.js"></script>
    <script src="js/mobile-angular-ui.min.js"></script>
    <!-- Required to use $touch, $swipe, $drag and $translate services -->
    <script src="js/mobile-angular-ui.gestures.min.js"></script>
    <script src="demo.js"></script>
  </head>

  <body>
<div ui-content-for="title">
  <span>Forms</span>
</div>

<div class="scrollable">
 <div class="scrollable-content section">
   
  <form role="form" ng-submit='login()'>
    <fieldset>
      <legend>Login</legend>
        <div class="form-group has-success has-feedback">
          <label>Email</label>
          <input type="email"
                 ng-model="email"
                 class="form-control"
                 placeholder="Enter email">
        </div>

        <div class="form-group">
          <label>Password</label>
          <input type="password" 
                 class="form-control"
                 placeholder="Password">
        </div>

        <div class="form-group">
          <label>Remember Me</label>
          <ui-switch 
            ng-model='rememberMe'></ui-switch>
        </div>
    </fieldset>
    <hr>

    <button class="btn btn-primary btn-block" type="submit">
      Login
    </button>


  </form>
 </div>
</div>

</body>
</html>