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
	                    message: 'Please provide Username'
	                }
	            }
	        },
	        pass:{
	        	validators :{
	        		notEmpty: {
	        			message:'Please set Password'
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
	                    message: 'Please enter the user Name'
	                }
	            }
	        },
	        loginpass: {
	        	validators:{
	        		notEmpty:{
	        			message: 'Please enter password'
	        		}
	        	}
	        }
	    }
	}),
	$("button#submit").click(function (){
		if($("#name").val()==""){}
		else
			{
				apiregister($("#name").val(), $("#pass").val());
			}
		
	}),
	$("form#loginform").on("submit", function (e){e.preventDefault();
	if($("#username").val()==""||$("#loginpass").val()==""){}
	else
		{
			apilogin($("#username").val(), $("#loginpass").val());
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
