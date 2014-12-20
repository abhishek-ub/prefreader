/**
 * @author Abhishek
 */

$(document).ready(function (){f=0;
	$('#prefs').bootstrapValidator({
	    feedbackIcons: {
	        valid: 'glyphicon glyphicon-ok',
	        invalid: 'glyphicon glyphicon-remove',
	        validating: 'glyphicon glyphicon-refresh'
	    },
	    fields: {
	        name: {
	            validators: {
	                notEmpty: {
	                    message: 'The first name is required and cannot be empty'
	                }
	            }
	        }
	    }
	}),
	$('#loginform').bootstrapValidator({
	    feedbackIcons: {
	        valid: 'glyphicon glyphicon-ok',
	        invalid: 'glyphicon glyphicon-remove',
	        validating: 'glyphicon glyphicon-refresh'
	    },
	    fields: {
	        username: {
	            validators: {
	                notEmpty: {
	                    message: 'The first name is required and cannot be empty'
	                }
	            }
	        }
	    }
	}),
	$("button#submit").click(function (){
		if($("#name").val()==""){}
		else
			{
			$.ajax({
				type: "POST",
				url: "RegistrationServlet",
				data: $("#prefs").serialize(),
				 success: function(msg){
	                $("#enter1").html(msg) ;//hide button and show thank you
	                $("#myModal").modal('hide'); //hide popup  
	                if($.trim(msg)==="Registration successfull!"){
	                	window.location.href="search.html";
	                	
	                	//ajax call to update similar user preferences
	                	$.ajax({
	                		type: "POST",
	                		url: "SimilarUsers",
	                		data:"",
	                		sucess:function(msg){
	                			//alert(msg);
	                		},error:function(){
	                			//alert("failed to capture similar users");
	                		}
	                		
	                	});
	                	
	                	
	                }
	                else
	                	alert(msg)
	            },
	            error: function(){
	                alert("failure");
	            }
			});
			}
		
	}),
	$("form#loginform").on("submit", function (e){e.preventDefault();
	if($("#username").val()==""){}
	else
		{
		$.ajax({
			type: "POST",
			url: "LoginServlet",
			data: "username="+$("#username").val(),
			 success: function(msg){
                $("#loginmodal").modal('hide'); //hide popup  
//                var user=$sessionScope.username;
//                alert("aaaasw"+user);
                if($.trim(msg)==="Login Successful!"){
                	window.location.href="search.html";
                }
                else
                	alert(msg);
            },
            error: function(){
                //alert("failure");
            }
		});
		}
	}),

	$('a[href^="#"]').on('click',function (e) {
	    e.preventDefault();

	    var target = this.hash;
	    $target = $(target);

	    $('html, body').stop().animate({
	        'scrollTop': $target.offset().top
	    }, 900, 'swing', function () {
	        window.location.hash = target;
	    });
	});
	
});



$(document).ready(function (){
	$('#ex1').slider({
	formatter: function(value) {
		return 'Current value: ' + value;
	}
});
} );
