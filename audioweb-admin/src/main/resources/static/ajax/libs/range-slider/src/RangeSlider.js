	$.fn.RangeSlider = function(cfg){
	var userAgent = navigator.userAgent.toLowerCase();
	var isWebkit = (userAgent.indexOf("edge") <0) && (userAgent.indexOf( "webkit" )> 0 );
	var isIE = isIE();
	
	function isIE() {
		var isIE = false;
		if (window.ActiveXObject || "ActiveXObject" in window) {
			isIE = true;
		} else {
			isIE = (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("msic") > -1
				&& !(userAgent.indexOf("opera") > -1));
			isIE = false;
		}
		return isIE;
	}
	
	this.sliderCfg = {
		min: cfg && !isNaN(parseFloat(cfg.min)) ? Number(cfg.min) : null, 
		max: cfg && !isNaN(parseFloat(cfg.max)) ? Number(cfg.max) : null,
		step: cfg && Number(cfg.step) ? cfg.step : 1,
		value: cfg && Number(cfg.value) ? cfg.value : 0,
		callback: cfg && cfg.callback ? cfg.callback : null,
		finishedCallback: cfg && cfg.finishedCallback ? cfg.finishedCallback : null
	};

	var $input = $(this);
	var min = this.sliderCfg.min;
	var max = this.sliderCfg.max;
	var step = this.sliderCfg.step;
	var value = this.sliderCfg.value;
	var callback = this.sliderCfg.callback;
	var finishedCallback = this.sliderCfg.finishedCallback;

	$input.attr('min', min)
		.attr('max', max)
		.attr('step', step)
		.attr('value',value);
	$input.css( 'background-size', value/max*100 + '% 100%' ); 
	var event = null;
	if (isIE) {
		event = "change";
	} else {
		event = "input";
	}
	
	$input.bind(event, function(e){
		$input.attr('value', this.value);
		
		if (isWebkit) {
			$input.css( 'background-size', this.value/max*100 + '% 100%' ); 
		}
		if ($.isFunction(callback)) {
			callback(this);
		}
	});
	$input.bind("mouseup", function(e){
		console.log("up");
		if ($.isFunction(callback)) {
			finishedCallback(this.value);
		}
	});	
	$input.bind("mousedowm", function(e){
		console.log("dowm");
	});
}