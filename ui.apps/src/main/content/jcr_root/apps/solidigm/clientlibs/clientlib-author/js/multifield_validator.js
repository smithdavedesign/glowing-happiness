$(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
  // check elements that has attribute data-foundation-validation with value starting with "multifield-max"
  selector: "[data-foundation-validation^='multifield-max']",
  validate: function(el) {
    // parse the max number from the attribute value
    var validationName = el.getAttribute("data-validation")
    var max = validationName.replace("multifield-max-", "");
    max = parseInt(max);
    if (el.items.length > max){
        // items added are more than allowed, return error
        return "Max allowed items is "+ max
    }
  }
});