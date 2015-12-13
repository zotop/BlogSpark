function getUrlParameter(param) {
    var pageUrl = window.location.search.substring(1);
    var urlVariables = pageUrl.split('&');
    for (var i = 0; i < urlVariables.length; i++)
    {
        var parameterName = urlVariables[i].split('=');
        if (parameterName[0] == param)
        {
            return parameterName[1];
        }
    }
}