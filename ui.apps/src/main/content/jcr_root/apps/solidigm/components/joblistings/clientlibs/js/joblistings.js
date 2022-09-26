window.addEventListener('DOMContentLoaded', async function () {
    window.alert('test');
    async function getTranslations(path) {
        const request = await fetch('/bin/solidigm/jobListingsTranslation?path=' + path);
        const translation = await request.json();

        return translation;
    }

    const path = document.querySelector('.cmp-joblistings').getAttribute('data-path');
    const translation = await getTranslations(path);
    window.dispatchEvent(
        new CustomEvent('translationsLoaded', {
            detail: { translation },
        })
    );

    window.addEventListener('translationsLoaded', function (event) {
        const translation = event.detail.translation;
        window.JobWidget = window.JobWidget || {};

        window.JobWidget.colorPickerArray = {
            bg_color_widget: '.srJobList',
            bg_color_headers: '.srJobListTitles th',
            txt_color_headers: '.srJobListTitles th nobr',
            bg_color_even_row: '.srJobList .srJobListJobEven *',
            bg_color_odd_row: '.srJobList .srJobListJobOdd *',
            txt_color_job: '.srJobListJobEven td, .srJobListJobOdd td',
            bg_color_links:
                '.srJobList .srJobListJobEven:hover *,.srJobList .srJobListJobOdd:hover *',
        };

        let load;
        window.widgetList = window.widgetList || [];
        window.widget =
            window.widget ||
            (function (window) {
                let jobNumber = 100;

                load = function () {
                    load.getScript(
                        'https://static.smartrecruiters.com/job-widget/1.6.0/script/jquery.min.js'
                    );
                    load.tryReady(0);
                };

                load.getScript = function (filename) {
                    let script = document.createElement('script');
                    script.type = 'text/javascript';
                    script.src = filename;
                    script.async = true;
                    if (script.readyState) {
                        script.onreadystatechange = function () {
                            if (this.readyState === 'loaded') {
                                jQuery.each(widgetStock, function (index, value) {
                                    let widgetElement = value;
                                    widget(
                                        widgetElement.widget_json,
                                        widgetElement.widget_element_id,
                                        widgetElement.widget_refresh_data,
                                        widgetElement.widget_callback
                                    );
                                });
                            }
                        };
                    } else {
                        script.onload = function () {
                            jQuery.each(widgetStock, function (index, value) {
                                let widgetElement = value;
                                if (widgetElement.widget_json !== undefined) {
                                    widget(
                                        widgetElement.widget_json,
                                        widgetElement.widget_element_id,
                                        widgetElement.widget_refresh_data,
                                        widgetElement.widget_callback
                                    );
                                }
                            });
                        };
                    }
                    document.getElementsByTagName('head')[0].appendChild(script);
                };

                load.tryReady = function (time_elapsed) {
                    if (typeof jQuery === 'undefined') {
                        if (time_elapsed <= 5000) {
                            setTimeout('load.tryReady(' + (time_elapsed + 200) + ')', 200);
                        } else {
                            alert('Timed out while loading jQuery.');
                        }
                    } else {
                        jQuery.noConflict();
                    }
                };

                if (typeof jQuery === 'undefined') {
                    load();
                }

                function defaultContent() {
                    return translate('filter_by');
                }
                let widgetLanguage = 'en';
                let widgetStock = [];
                let widgetJSON = {};
                let filerJSON = {};
                let numberOfWidgets = 0;
                let careerTable;

                function widget(json, element_id, refresh_data, callback) {
                    let widgetElement;
                    let localWidgetList;
                    let widgetID;

                    widgetLanguage = determineWidgetLanguage(json);

                    if (typeof jQuery === 'undefined') {
                        widgetStock.push({
                            widget_json: json,
                            widget_element_id: element_id,
                            widget_refresh_data: refresh_data,
                            widget_callback: callback,
                        });
                    } else {
                        localWidgetList = jQuery('.job_widget');
                        if (localWidgetList.length == 0) {
                            localWidgetList = jQuery("[src$='/smart_widget.js']").next('script');
                        }
                        localWidgetList.each(function (index) {
                            jQuery(this).attr('id', 'job_widget_' + index);
                        });
                        if (window.widgetList == undefined) {
                            window.widgetList = new Array();
                        }

                        loadCSS(json.custom_css_url);

                        if (element_id == undefined || jQuery('#' + element_id).length == 0) {
                            let div = document.createElement('div');
                            div.setAttribute('class', 'smartWidget');
                            div.id = 'smartWidget' + numberOfWidgets;
                            element_id = div.id;
                            jQuery(jQuery('#job_widget_' + numberOfWidgets)).after(jQuery(div));
                            numberOfWidgets++;
                            widgetElement = jQuery(div);
                        } else {
                            widgetID = '#' + element_id;
                            widgetElement = jQuery(widgetID);
                        }
                        widgetElement.html('');
                        widgetElement.addClass('loading');
                        widgetJSON[element_id] = json;
                        initFilterValues(json, element_id);
                        setCurrentPage(element_id, 0);
                        setOffset(element_id, 0);
                        setLimit(element_id, json.jobs_number);
                        if (refresh_data == undefined || refresh_data) {
                            if (
                                (json.company_code !== undefined && json.company_code.length > 0) ||
                                (json.group_id !== undefined && json.group_id.length > 0)
                            ) {
                                executeQuery(element_id, null, callback);
                                addEvents(element_id);
                            }
                        } else {
                            data = window.widgetList[element_id];
                            createWidgetContent(
                                json.company_code,
                                data,
                                element_id,
                                json,
                                widgetElement,
                                callback,
                                false
                            );
                        }
                    }
                }

                function initFilterValues(json, widgetID) {
                    filerJSON[widgetID] = {};
                    if (json) {
                        if (json.filter_departments_name) {
                            filerJSON[widgetID].filter_departments = json.filter_departments_name;
                        }
                        if (json.filter_locations) {
                            filerJSON[widgetID].filter_locations = json.filter_locations;
                        }
                        if (json.filter_languages) {
                            filerJSON[widgetID].filter_languages = json.filter_languages;
                        }
                        for (let key in json) {
                            if (isCustomField(key)) {
                                filerJSON[widgetID][key] = json[key];
                            }
                        }
                    }
                }

                function toList(obj) {
                    if (typeof obj === 'string') {
                        return [obj];
                    } else {
                        return obj || [];
                    }
                }

                function filterValAllElements(array) {
                    let hasValAll = false;
                    for (let i = 0; i < array.length; i++) {
                        if (array[i] && array[i].indexOf('val_all_') >= 0) {
                            hasValAll = true;
                            break;
                        }
                    }
                    return hasValAll ? [] : array;
                }

                function executeQuery(
                    widgetID,
                    queryString,
                    callback,
                    offset,
                    limit,
                    remoteFilter
                ) {
                    let json = extendWithFilters(widgetJSON[widgetID], widgetID);
                    let company_code = json.company_code;
                    let group_code = json.group_id;
                    let jobListURL = new String('');
                    let domain;
                    let locationType;
                    offset = offset || 0;
                    limit = limit || getLimit(widgetID) || jobNumber;
                    locationType = remoteFilter ? 'REMOTE' : 'ANY';

                    if (json.api_url && json.api_url.length > 0) {
                        domain = json.api_url;
                    } else {
                        domain = 'https://api.smartrecruiters.com';
                    }
                    if (company_code) {
                        jobListURL = jobListURL
                            .concat(domain)
                            .concat('/job-api/public/search/widgets/')
                            .concat(company_code)
                            .concat('/postings?');
                    } else if (group_code) {
                        jobListURL = jobListURL
                            .concat(domain)
                            .concat('/job-api/public/search/companyGroups/')
                            .concat(group_code)
                            .concat('/postings?');
                    } else {
                        return;
                    }
                    if (queryString && queryString !== defaultContent()) {
                        jobListURL += 'q=' + queryString;
                    }
                    let keyFilterTranslation = {
                        filter_locations: 'location',
                        filter_companies: 'company_name',
                        filter_departments_name: 'facet_department',
                        filter_languages: 'languageCode',
                    };

                    if (offset !== undefined) {
                        jobListURL = jobListURL.concat('&offset=').concat(offset);
                    }

                    if (limit !== undefined) {
                        jobListURL = jobListURL.concat('&limit=').concat(limit);
                    }

                    let filterArray = [];
                    let items;

                    for (let key in json) {
                        items = [];
                        if (isFilterParam(key, keyFilterTranslation)) {
                            if (filerJSON && filerJSON[widgetID] && filerJSON[widgetID][key]) {
                                let filters = filerJSON[widgetID][key];
                                if (jQuery.isArray(filters)) {
                                    items = filters;
                                } else {
                                    items = [filters];
                                }
                            }
                            items = filterValAllElements(items);
                            if (items && items.length > 0) {
                                filterArray.push(
                                    getParamNameForFilter(key, keyFilterTranslation) +
                                        ':(' +
                                        encodeURIComponent(items.join(';')) +
                                        ')'
                                );
                            }
                        }
                    }

                    if (json.auth) {
                        jobListURL = jobListURL.concat('&token=').concat(json.auth);
                    }

                    if (json.jobAdType) {
                        jobListURL = jobListURL.concat('&jobAdType=').concat(json.jobAdType);
                    }

                    jobListURL = jobListURL.concat('&fq=').concat(filterArray.join(','));
                    jobListURL = jobListURL
                        .concat('&customFields=')
                        .concat(
                            resolveDepartmentsFilter(
                                json.departments_field_id,
                                json.filter_departments
                            )
                        );
                    jobListURL = jobListURL.concat('&langCode=').concat(widgetLanguage);
                    jobListURL = jobListURL.concat('&locationType=').concat(locationType);
                    console.log(jobListURL);
                    let widgetElement = jQuery('#' + widgetID);
                    jQuery.ajax({
                        url: jobListURL,
                        dataType: 'jsonp',
                        type: 'GET',
                        success: function (data) {
                            let dataFiltered = data.results.filter((obj) => {
                                return obj.department !== 'SKH MSA';
                            });
                            data.results = dataFiltered;
                            window.widgetList[widgetID] = data;
                            createWidgetContent(
                                company_code,
                                data,
                                widgetID,
                                json,
                                widgetElement,
                                callback,
                                remoteFilter
                            );
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            createEmptyWidget();
                        },
                    });
                }

                function resolveDepartmentsFilter(departmentsFieldId, departmentFilterIds) {
                    if (!departmentsFieldId || !departmentFilterIds) {
                        return '';
                    }

                    let departmentFilters = [];
                    let departmentIds = jQuery.isArray(departmentFilterIds)
                        ? departmentFilterIds
                        : [departmentFilterIds];
                    for (let i = 0; i < departmentIds.length; i++) {
                        departmentFilters.push(departmentsFieldId + '|' + departmentIds[i]);
                    }

                    return encodeURIComponent(departmentFilters.join(','));
                }

                function extendWithFilters(orginalJson, widgetID) {
                    let json = orginalJson;
                    if (filerJSON && filerJSON[widgetID]) {
                        json = jQuery.extend({}, orginalJson, filerJSON[widgetID]);
                    }
                    return json;
                }

                let customFieldRegexp = /^custom_field_(.*)/;

                function isFilterParam(key, translations) {
                    if (key) {
                        return translations[key] || key.match(customFieldRegexp);
                    }
                    return false;
                }

                function isCustomField(key) {
                    if (key) {
                        return key.match(customFieldRegexp);
                    }
                    return false;
                }

                function getParamNameForFilter(key, translations) {
                    let customFieldMatch = key.match(customFieldRegexp);
                    if (customFieldMatch) {
                        return 'custom_field_value_id_' + customFieldMatch[1];
                    } else {
                        return translations[key];
                    }
                }

                function addEvents(widgetID) {
                    let remoteFilter;
                    jQuery('#' + widgetID).undelegate('.srSearchOptionListElement', 'click');
                    jQuery('#' + widgetID).undelegate('.srFilterRemoteElement', 'change');
                    jQuery('#' + widgetID).undelegate('.srSearch .srSearchOption', 'click');
                    jQuery('#' + widgetID).undelegate('.srSearchForm', 'submit');
                    jQuery('#' + widgetID).undelegate('.srSearchButton', 'click');
                    jQuery('#' + widgetID).undelegate('.srSearchInput', 'keypress');
                    jQuery('html').unbind('click');
                    jQuery('#' + widgetID).delegate(
                        '.srSearchOptionListElement',
                        'click',
                        function () {
                            let selectedElement = jQuery(this)
                                .find('.srSearchOptionListElementText')
                                .html();
                            remoteFilter = jQuery('.srFilterRemoteElement').hasClass(
                                'srFilterRemoteElementChecked'
                            );
                            jQuery(this).siblings().removeClass('srSearchOptionListElementChecked');
                            if (jQuery(this).hasClass('srSearchOptionListElementChecked')) {
                                jQuery(this).removeClass('srSearchOptionListElementChecked');
                            } else {
                                jQuery(this)
                                    .parents('ul')
                                    .prev()
                                    .html(add3Dots(selectedElement, 10));
                                jQuery(this).addClass('srSearchOptionListElementChecked');
                            }
                            setCurrentPage(widgetID, 0);
                            executeQueryWithFilter(this, remoteFilter);
                        }
                    );
                    jQuery('#' + widgetID).delegate(
                        '.srSearch .srSearchOption',
                        'click',
                        function () {
                            let listElement = jQuery(this).children('.srSearchOptionList');
                            listElement.children().length > 0
                                ? listElement.removeClass('none')
                                : listElement.addClass('none');
                            if (jQuery(this).children('.srSearchOptionList').is(':visible')) {
                                jQuery(this).children('.srSearchOptionList').slideUp('fast');
                            } else {
                                jQuery(this).children('.srSearchOptionList').slideDown('fast');
                            }
                            return false;
                        }
                    );
                    jQuery('#' + widgetID).delegate('.srSearchForm', 'submit', function (e) {
                        e.preventDefault();
                    });
                    jQuery('#' + widgetID).delegate('.srSearchButton', 'click', function () {
                        setCurrentPage(widgetID, 0);
                        executeQueryWithFilter(this, remoteFilter);
                    });
                    jQuery('#' + widgetID).delegate('.srSearchInput', 'keypress', function (e) {
                        if (e.keyCode == 13) {
                            setCurrentPage(widgetID, 0);
                            executeQueryWithFilter(this, remoteFilter);
                        }
                    });
                    jQuery('#' + widgetID).delegate(
                        '.srFilterRemoteElement',
                        'change',
                        function (e) {
                            if (jQuery(e.target).hasClass('srFilterRemoteElementChecked')) {
                                jQuery(e.target).removeClass('srFilterRemoteElementChecked');
                                setCurrentPage(widgetID, 0);
                                executeQueryWithFilter(this, false);
                            } else {
                                jQuery(e.target).addClass('srFilterRemoteElementChecked');
                                setCurrentPage(widgetID, 0);
                                executeQueryWithFilter(this, true);
                            }
                        }
                    );
                    jQuery('html').bind('click', function () {
                        jQuery('.srSearchOptionList').slideUp('fast');
                    });
                }

                function getWidgetId(element) {
                    return jQuery(element).parents('.smartWidget, #widget_content').attr('id');
                }

                function getCurrentPage(id) {
                    return filerJSON[id].currentPage;
                }

                function setCurrentPage(id, value) {
                    filerJSON[id].currentPage = value;
                }

                function getPageCount(id) {
                    return filerJSON[id].pageCount;
                }

                function setPageCount(id, value) {
                    filerJSON[id].pageCount = value;
                }

                function getOffset(id) {
                    return filerJSON[id].offset;
                }

                function setOffset(id, value) {
                    filerJSON[id].offset = value;
                }

                function getLimit(id) {
                    return filerJSON[id].limit;
                }

                function setLimit(id, value) {
                    filerJSON[id].limit = value;
                }

                function executeQueryWithFilter(filter, remoteFilter) {
                    let filterObject = jQuery(filter);
                    let widgetID = filterObject.parents('.smartWidget, #widget_content').attr('id');
                    let filerID = filterObject.parents('.srSearchOption').attr('id');
                    if (!filerJSON[widgetID]) {
                        filerJSON[widgetID] = {};
                    }
                    if (
                        filterObject.hasClass('srSearchOptionListElementChecked') &&
                        !filterObject.hasClass('srSearchOptionClearListElement')
                    ) {
                        filerJSON[widgetID][keyFacetToFilterTranslation[filerID]] = filterObject
                            .children('.srSearchOptionListElementText')
                            .attr('data-filter-value');
                    } else {
                        filerJSON[widgetID][keyFacetToFilterTranslation[filerID]] = restoreFilter(
                            widgetID,
                            keyFacetToFilterTranslation[filerID]
                        );
                    }
                    let queryString = escapeHtml(
                        jQuery('#' + widgetID)
                            .find('.srSearchInput')
                            .val() || ''
                    );
                    if (queryString !== defaultContent()) {
                        filerJSON[widgetID]['queryString'] = queryString;
                    } else {
                        filerJSON[widgetID]['queryString'] = '';
                    }

                    executeQuery(widgetID, queryString, null, null, null, remoteFilter);
                }

                function executeCurrentQuery(offset, limit, widgetId) {
                    let queryString = filerJSON[widgetId]['queryString'];
                    executeQuery(widgetId, queryString, null, offset, limit);
                }

                function loadCSS(url) {
                    let targetelement = 'link';
                    let targetattr = 'href';
                    let allsuspects = document.getElementsByTagName(targetelement);
                    let present = false;
                    for (let i = allsuspects.length; i >= 0; i--) {
                        if (
                            allsuspects[i] &&
                            allsuspects[i].getAttribute(targetattr) != null &&
                            allsuspects[i].getAttribute(targetattr) == url
                        ) {
                            present = true;
                        }
                    }
                    if (!present) {
                        let link = document.createElement('link');
                        link.rel = 'stylesheet';
                        link.type = 'text/css';
                        link.media = 'screen';
                        link.setAttribute('href', url);
                        if (typeof link !== 'undefined') {
                            document.getElementsByTagName('head')[0].appendChild(link);
                        }
                    }
                }

                function populateColorInputs() {
                    for (let key in window.JobWidget.colorPickerArray) {
                        if (window.JobWidget.colorPickerArray.hasOwnProperty(key)) {
                            let fieldID = key + '_field';
                            let selectorID = key + '_selector';
                            if (key.indexOf('bg_color') === 0) {
                                css = 'backgroundColor';
                            } else {
                                css = 'color';
                            }
                            if (
                                jQuery('#' + fieldID).val() != undefined &&
                                jQuery('#' + fieldID).val().length != 0
                            ) {
                                let hex = jQuery('#' + fieldID).val();
                                let colorString = hex2rgb(hex);
                            } else {
                                let colorString = jQuery(
                                    window.JobWidget.colorPickerArray[key]
                                ).css(css);
                                if (colorString != undefined && colorString.substr(0, 1) == '#') {
                                    let hex = colorString;
                                    colorString = hex2rgb(colorString);
                                } else {
                                    let hex = rgb2hex(colorString);
                                }
                            }
                            if (colorString != undefined) {
                                jQuery('#' + fieldID).val(hex);
                                jQuery('#' + fieldID).css('backgroundColor', colorString);
                                jQuery('#' + fieldID)
                                    .parents('.srWidgetInputContainer')
                                    .css('backgroundColor', hex);
                                jQuery('#' + fieldID)
                                    .prevAll('.jobWidgetPageColorField')
                                    .css('backgroundColor', hex);
                                //jQuery("#" + fieldID).parents(".srWidgetInputContainer").find("div").css('backgroundColor', hex);
                                jQuery('#' + selectorID).ColorPickerSetColor(hex);
                            }
                        }
                    }
                }

                function rgb2hex(rgb) {
                    if (rgb != undefined) {
                        rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
                        if (rgb != null) {
                            return '#' + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
                        } else {
                            return '';
                        }
                    }
                    return '';
                }

                function hex(x) {
                    return ('0' + parseInt(x).toString(16)).slice(-2);
                }

                function hex2rgb(hex) {
                    let c,
                        o = [],
                        k = 0,
                        m = hex.match(
                            /^#(([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})|([0-9a-f])([0-9a-f])([0-9a-f]))$/i
                        );
                    if (!m) return 'rgb(0,0,0)';
                    for (let i = 2, s = m.length; i < s; i++) {
                        if (undefined === m[i]) continue;
                        c = parseInt(m[i], 16);
                        o[k++] = c + (i > 4 ? c * 16 : 0);
                    }
                    return 'rgb(' + o[0] + ',' + o[1] + ',' + o[2] + ')';
                }

                function findCustomFieldName(data) {
                    if (!data.results || !data.results.length > 0) {
                        return 'Custom Field';
                    }

                    for (let i = 0; i < data.results.length; i++) {
                        let cfValues = data.results[i].customFieldValues;
                        if (cfValues && cfValues.length > 0 && cfValues[0].fieldLabel) {
                            return cfValues[0].fieldLabel;
                        }
                    }

                    return 'Custom Field';
                }

                function createWidgetContent(
                    companyIdentifier,
                    data,
                    widgetID,
                    json,
                    widgetElement,
                    callback,
                    remoteFilter
                ) {
                    if (data === undefined) {
                        return false;
                    }
                    let htmlContent = new String('');
                    if (json.custom_css_url != undefined && json.custom_css_url.length > 0) {
                        htmlContent +=
                            "<link href='" +
                            json.custom_css_url +
                            "' type='text/css' rel='stylesheet'>";
                    }
                    //SearchBar
                    if (json.add_search && json.add_search === 'true') {
                        htmlContent += createSearchBar(data, widgetID, json, remoteFilter);
                    }

                    //Content Table
                    //Columns Header
                    htmlContent +=
                        "<table class='srJobList' style='display:none;' id='careersTable'>";
                    if (
                        json.remove_headers == undefined ||
                        (json.remove_headers != undefined && json.remove_headers != 'true')
                    ) {
                        json.$customFieldName = findCustomFieldName(data);
                        htmlContent = htmlContent + createHeader(json);
                    }
                    //Data
                    let countOfItems = 0;
                    let countOnPage = json.jobs_number || jobNumber;
                    let domainn;
                    if (data.results != undefined) {
                        countOfItems = data.results.length;

                        if (json.api_url && json.api_url.length > 0) {
                            domainn = json.api_url;
                        } else {
                            domainn = 'http://www.smartrecruiters.com';
                        }

                        let translatedDepartments = mapDepartmentsById(data);
                        jQuery.each(data.results, function (i, vacancy) {
                            let link =
                                '"' +
                                domainn +
                                '/' +
                                vacancy.companyIdentifier +
                                '/' +
                                vacancy.publicationId;

                            // if is internal
                            if (
                                vacancy &&
                                vacancy.destinationCode &&
                                vacancy.destinationCode.toLowerCase() === 'internal_widget'
                            ) {
                                link =
                                    '"' +
                                    domainn +
                                    '/ni/' +
                                    vacancy.companyIdentifier +
                                    '/' +
                                    vacancy.uuid;
                            }

                            if (vacancy.urlJobName) {
                                link = link + '-' + vacancy.urlJobName;
                            }

                            if (json.trid) {
                                link += '?trid=' + json.trid;
                            }

                            link += '"';

                            if (i % 2 === 0) {
                                htmlContent +=
                                    "<tr class='srJobListJobOdd' onClick ='window.open(" +
                                    link +
                                    ");'>";
                            } else {
                                htmlContent +=
                                    "<tr class='srJobListJobEven' onClick ='window.open(" +
                                    link +
                                    ");'>";
                            }
                            if (json.job_title !== 'undefined' && json.job_title == 'true') {
                                htmlContent += buildContentElement(
                                    'srJobListJobTitle',
                                    escapeHtml(vacancy.vacancyName || '')
                                );
                            }
                            if (typeof json.group_id != 'undefined' && json.group_id.length > 0) {
                                htmlContent += buildContentElement(
                                    'srJobListCompanyName',
                                    escapeHtml(vacancy.companyName || '')
                                );
                            }
                            if (json.ref_number !== 'undefined' && json.ref_number == 'true') {
                                htmlContent += buildContentElement(
                                    'srJobListRefNumber',
                                    escapeHtml(vacancy.refNumber || '')
                                );
                            }
                            if (
                                json.type_of_employment != 'undefined' &&
                                json.type_of_employment == 'true'
                            ) {
                                htmlContent += buildContentElement(
                                    'srJobListTypeOfEmployment',
                                    escapeHtml(vacancy.typeOfEmployment || '')
                                );
                            }
                            if (json.department != 'undefined' && json.department == 'true') {
                                let label =
                                    translatedDepartments[vacancy.departmentId] ||
                                    vacancy.department;
                                htmlContent += buildContentElement(
                                    'srJobListDepartment',
                                    escapeHtml(label || '')
                                );
                            }
                            if (json.location != 'undefined' && json.location == 'true') {
                                let location = buildLocation(vacancy);
                                vacancy.locationRemote
                                    ? (htmlContent += buildContentElement(
                                          'srJobListLocation srRemoteLocation',
                                          escapeHtml(location || ''),
                                          translate('remote_info')
                                      ))
                                    : (htmlContent += buildContentElement(
                                          'srJobListLocation',
                                          escapeHtml(location || '')
                                      ));
                            }
                            if (
                                json.occupational_area !== undefined &&
                                json.occupational_area == 'true'
                            ) {
                                htmlContent += buildContentElement(
                                    'srJobListOccupationalArea',
                                    vacancy.occupationalArea || ''
                                );
                            }
                            if (
                                json.published_since != 'undefined' &&
                                json.published_since == 'true'
                            ) {
                                let releaseDate = vacancy.releasedDate;
                                let formattedDate = '';
                                if (releaseDate && json.dateFormat === 'eu') {
                                    formattedDate = formatToEuDate(new Date(releaseDate));
                                } else {
                                    formattedDate = formatToUsDate(new Date(releaseDate));
                                }
                                htmlContent += buildContentElement(
                                    'srJobListPublishedSince',
                                    formattedDate
                                );
                            }
                            if (json.custom_field != 'undefined' && json.custom_field == 'true') {
                                let customValueLabel =
                                    vacancy.customFieldValues &&
                                    vacancy.customFieldValues.length > 0
                                        ? vacancy.customFieldValues[0].valueLabel
                                        : '';
                                htmlContent += buildContentElement(
                                    'srJobListCustomField',
                                    customValueLabel
                                );
                            }
                            htmlContent += '</tr>';
                        });
                    }
                    htmlContent += '</table>';

                    if (json.add_search && json.add_search === 'true') {
                        // htmlContent =  htmlContent + createPagination(countOfItems, countOnPage, widgetID);
                    }
                    jQuery('.srPagesTextPrevious').unbind('click');
                    jQuery('.srPagesTextNext').unbind('click');

                    widgetElement.html(htmlContent);

                    jQuery('.srPagesTextPrevious').bind('click', prev);
                    jQuery('.srPagesTextNext').bind('click', next);

                    let link = widgetElement.find('link');
                    if (link.length > 0) {
                        link.after(createCSS(json, widgetElement));
                    } else {
                        widgetElement.prepend(createCSS(json, widgetElement));
                    }
                    widgetElement.find('table').fadeIn('fast', function () {
                        if (callback != undefined && jQuery.isFunction(eval(callback))) {
                            eval(callback).call();
                        }
                    });
                    widgetElement.removeClass('loading');
                    if (
                        typeof jQuery('#widget_content').parents('#srWidgetPreviewContainer')
                            .AjaxIndicator == 'function'
                    ) {
                        jQuery('#widget_content')
                            .parents('#srWidgetPreviewContainer')
                            .AjaxIndicator('hide');
                    }
                    jQuery('.srSearchOptionList').each(function () {
                        if (jQuery(this).css('height').replace('px', '') == 0) {
                            jQuery(this).addClass('none');
                        } else if (jQuery(this).css('height').replace('px', '') <= 240) {
                            jQuery(this).css('overflow-y', 'auto');
                            jQuery(this).removeClass('none');
                        } else {
                            jQuery(this).css('overflow-y', 'scroll');
                            jQuery(this).removeClass('none');
                        }
                    });
                    jQuery('.srSearchOptionList .srSearchOptionListElementChecked').each(
                        function () {
                            jQuery(this)
                                .parent()
                                .prev('.srSearchOptionText')
                                .html(
                                    add3Dots(
                                        jQuery(this).find('.srSearchOptionListElementText').html(),
                                        10
                                    )
                                );
                        }
                    );
                    function getLanguageCode() {
                        if (window.location.href.includes('com.cn')) {
                            return 'https://cdn.datatables.net/plug-ins/1.12.1/i18n/zh.json';
                        } else if (window.location.href.includes('cn/zh')) {
                            return 'https://cdn.datatables.net/plug-ins/1.12.1/i18n/zh.json';
                        } else if (window.location.href.includes('.de/de')) {
                            return 'https://cdn.datatables.net/plug-ins/1.12.1/i18n/de-DE.json';
                        } else if (window.location.href.includes('kr/ko')) {
                            return 'https://cdn.datatables.net/plug-ins/1.12.1/i18n/ko.json';
                        } else if (window.location.href.includes('jp/ja')) {
                            return 'https://cdn.datatables.net/plug-ins/1.12.1/i18n/ja.json';
                        }
                        return '';
                    }
                    let langText = getLanguageCode();
                    $('#careersTable').DataTable({
                        iDisplayLength: 10,
                        retrieve: true,
                        language: {
                            url: langText,
                        },
                    });
                    return true;
                }

                function mapDepartmentsById(data) {
                    return mapDepartments(data, 'externalId');
                }

                function mapDepartmentsByLabel(data) {
                    return mapDepartments(data, 'label');
                }

                function mapDepartments(data, field) {
                    let departments = {};
                    if (data.companyDepartments) {
                        jQuery.each(data.companyDepartments, function (index, value) {
                            departments[value[field]] = value.translatedLabel;
                        });
                    }
                    return departments;
                }

                function buildLocation(vacancy) {
                    let location = [];
                    location.push(vacancy.location);
                    if (
                        vacancy.regionAbbreviation &&
                        vacancy.countryAbbreviation &&
                        (vacancy.countryAbbreviation.toLowerCase() === 'us' ||
                            vacancy.countryAbbreviation.toLowerCase() === 'au')
                    ) {
                        location.push(vacancy.regionAbbreviation);
                    } else if (vacancy.countryName) {
                        location.push(vacancy.countryName);
                    }
                    return location.join(', ');
                }

                function buildContentElement(className, content, title) {
                    return title
                        ? "<td class='" + className + "' title='" + title + "'>" + content + '</td>'
                        : "<td class='" + className + "'>" + content + '</td>';
                }

                function createEmptyWidget() {}

                function createCSS(json, widgetElement) {
                    let ss1 = document.createElement('style');
                    ss1.setAttribute('type', 'text/css');

                    let styleContent = '';
                    styleContent += '#' + widgetElement.attr('id') + '{';
                    styleContent += addSizeRule('width', json.widget_width);
                    styleContent += addSizeRule('width', json.auto_width, true);
                    styleContent += addSizeRule('height', json.widget_height);
                    styleContent += addSizeRule('height', json.auto_height, true);
                    if (json.widget_height > 0 || json.widget_width > 0) {
                        styleContent += addRule('overflow', 'hidden');
                    }
                    styleContent += '}\n';
                    styleContent += '\n#' + widgetElement.attr('id') + ' .srJobList{';
                    styleContent += addRule('background-color', json.bg_color_widget);
                    styleContent += addSizeRule('width', json.widget_width);
                    styleContent += addSizeRule('width', json.auto_width);
                    styleContent += addSizeRule('height', json.widget_height);
                    styleContent += addSizeRule('height', json.auto_height, true);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListTitles *{';
                    styleContent += addRule('background-color', json.bg_color_headers);
                    styleContent += addRule('color', json.txt_color_headers);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobOdd *{';
                    styleContent += addRule('background-color', json.bg_color_odd_row);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobEven *{';
                    styleContent += addRule('background-color', json.bg_color_even_row);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobEven td{';
                    styleContent += addRule('color', json.txt_color_job);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobOdd td{';
                    styleContent += addRule('color', json.txt_color_job);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobEven:hover *{';
                    styleContent += addRule('background-color', json.bg_color_links);
                    styleContent += '}\n';

                    styleContent +=
                        '#' + widgetElement.attr('id') + ' .srJobList .srJobListJobOdd:hover *{';
                    styleContent += addRule('background-color', json.bg_color_links);
                    styleContent += '}\n';

                    if (ss1.styleSheet) {
                        ss1.styleSheet.cssText = styleContent;
                    } else {
                        let tt1 = document.createTextNode(styleContent);
                        ss1.appendChild(tt1);
                    }
                    return ss1;
                }

                function getObjectCss() {
                    let css = null;
                    try {
                        css = document.styleSheets[0];
                        if (!css) {
                            let head = document.getElementsByTagName('head').item(0);
                            head.appendChild(document.createElement('style'));
                            css = document.styleSheets[0];
                        }
                    } catch (ex) {
                        css = document.createStyleSheet('styles.css');
                    }
                    return css;
                }

                function addRule(style, value) {
                    if (value != undefined) {
                        return style + ':' + value + ';\n';
                    } else {
                        return '';
                    }
                }

                function addSizeRule(style, value, real_value) {
                    if (value != undefined) {
                        if (value == 'auto') {
                            if (real_value == true) {
                                return style + ':' + value + ';\n';
                            } else {
                                return style + ':100%;\n';
                            }
                        } else {
                            return style + ':' + parseInt(value) + 'px;\n';
                        }
                    } else {
                        return '';
                    }
                }

                let keyFacetTranslation = {
                    facet_location: 'location',
                    facet_company: 'company_name',
                    facet_department: 'department',
                };

                let keyFacetToFilterTranslation = {
                    facet_location: 'filter_locations',
                    facet_company: 'filter_companies',
                    facet_department: 'filter_departments_name',
                };

                function createSearchBar(data, widgetID, json, remoteFilter) {
                    let group_code = json.group_id;
                    let htmlContent = '';
                    let queryString;
                    let departments = mapDepartmentsByLabel(data);
                    if (filerJSON && filerJSON[widgetID] && filerJSON[widgetID]['queryString']) {
                        queryString = filerJSON[widgetID]['queryString'];
                    } else {
                        queryString = defaultContent();
                    }
                    htmlContent += '<div class="srSearch">';
                    for (let facet in keyFacetTranslation) {
                        if (!(!group_code && facet === 'facet_company')) {
                            htmlContent += '<div class="srSearchOption" id="' + facet + '">';
                            htmlContent +=
                                '<span class="srSearchOptionText">' +
                                add3Dots(translate(keyFacetTranslation[facet]), 10) +
                                '</span>';
                            htmlContent += '<ul class="srSearchOptionList">';
                            let facetElements = data.facets[facet];

                            let facetFilter =
                                filerJSON[widgetID][keyFacetToFilterTranslation[facet]];
                            let defaultFilter = restoreFilter(
                                widgetID,
                                keyFacetToFilterTranslation[facet]
                            );
                            if (facetFilter && facetFilter !== defaultFilter) {
                                htmlContent +=
                                    '<li class="srSearchOptionListElement srSearchOptionClearListElement">';
                                htmlContent +=
                                    '<span class="srSearchOptionListElementText">' +
                                    translate('view_all') +
                                    '</span>';
                                htmlContent += '</li>';
                            }
                            for (let facetElement in facetElements) {
                                let filters = toList(
                                    filerJSON[widgetID][keyFacetToFilterTranslation[facet]]
                                );
                                if (
                                    filerJSON[widgetID] &&
                                    filters.length == 1 &&
                                    facetElement === filters[0]
                                ) {
                                    htmlContent +=
                                        '<li class="srSearchOptionListElement srSearchOptionListElementChecked">';
                                } else {
                                    htmlContent += '<li class="srSearchOptionListElement">';
                                }
                                if (facet === 'facet_department') {
                                    let label = departments[facetElement] || facetElement;
                                    htmlContent +=
                                        '<span data-filter-value="' +
                                        escapeHtml(facetElement) +
                                        '" class="srSearchOptionListElementText">' +
                                        escapeHtml(label) +
                                        '</span>';
                                } else {
                                    htmlContent +=
                                        '<span data-filter-value="' +
                                        escapeHtml(facetElement) +
                                        '" class="srSearchOptionListElementText">' +
                                        escapeHtml(facetElement) +
                                        '</span>';
                                }
                                htmlContent += '</li>';
                            }
                            htmlContent += '</ul>';
                            htmlContent += '</div>';
                        }
                    }
                    htmlContent += '<div class="srFilterRemote">';
                    !!remoteFilter
                        ? (htmlContent +=
                              '<input id="location_remote" type="checkbox" class="srFilterRemoteElement srFilterRemoteElementChecked" checked>')
                        : (htmlContent +=
                              '<input id="location_remote" type="checkbox" class="srFilterRemoteElement">');
                    htmlContent +=
                        '<label for="location_remote" class="srFilterRemoteElementText">' +
                        translate('remote_info') +
                        '</label>';
                    htmlContent += '</div>';

                    htmlContent += '</div>';
                    return htmlContent;
                }

                function createHeader(json) {
                    let header = "<thead><tr class='srJobListTitles'>";
                    if (json.job_title != 'undefined' && json.job_title == 'true') {
                        header = header + buildHeaderColumn('srJobListJobTitle', 'job_title');
                    }
                    if (typeof json.group_id != 'undefined' && json.group_id.length > 0) {
                        header = header + buildHeaderColumn('srJobListCompanyName', 'company_name');
                    }
                    if (json.ref_number === 'true') {
                        header = header + buildHeaderColumn('srJobListRefNumber', 'ref_number');
                    }
                    if (
                        json.type_of_employment != 'undefined' &&
                        json.type_of_employment == 'true'
                    ) {
                        header =
                            header +
                            buildHeaderColumn('srJobListTypeOfEmployment', 'type_of_employment');
                    }
                    if (json.department != 'undefined' && json.department == 'true') {
                        header = header + buildHeaderColumn('srJobListDepartment', 'department');
                    }
                    if (json.location != 'undefined' && json.location == 'true') {
                        header = header + buildHeaderColumn('srJobListLocation', 'location');
                    }
                    if (json.occupational_area != 'undefined' && json.occupational_area == 'true') {
                        header =
                            header +
                            buildHeaderColumn('srJobListOccupationalArea', 'occupational_area');
                    }
                    if (json.published_since != 'undefined' && json.published_since == 'true') {
                        header =
                            header +
                            buildHeaderColumn('srJobListPublishedSince', 'published_since');
                    }
                    if (json.custom_field != 'undefined' && json.custom_field == 'true') {
                        header =
                            header +
                            buildHeaderColumn('srJobListCustomField', json.$customFieldName);
                    }
                    header = header + '</tr></thead>';
                    return header;
                }

                function escapeHtml(unsafe) {
                    return unsafe
                        .replace(/&/g, '&amp;')
                        .replace(/</g, '&lt;')
                        .replace(/>/g, '&gt;')
                        .replace(/"/g, '&quot;')
                        .replace(/'/g, '&#039;');
                }

                function buildHeaderColumn(className, translation_key) {
                    return (
                        "<th class='" +
                        className +
                        "'><nobr>" +
                        translate(translation_key) +
                        '</nobr></th>'
                    );
                }

                function prev(e) {
                    let id = getWidgetId(this);
                    let currentPage = getCurrentPage(id);
                    if (currentPage > 0) {
                        currentPage -= 1;
                    }
                    let limit = getLimit(id);
                    let offset = currentPage * limit;
                    setCurrentPage(id, currentPage);
                    setOffset(id, offset);
                    executeCurrentQuery(offset, limit, id);

                    e.preventDefault();
                    return false;
                }
                function next(e) {
                    let id = getWidgetId(this);
                    let currentPage = getCurrentPage(id);
                    let pageCount = getPageCount(id);
                    if (currentPage < pageCount - 1) {
                        currentPage += 1;
                    }
                    let limit = getLimit(id);
                    let offset = currentPage * limit;
                    setCurrentPage(id, currentPage);
                    setOffset(id, offset);
                    executeCurrentQuery(offset, limit, id);

                    e.preventDefault();
                    return false;
                }

                function add3Dots(string, limit) {
                    let dots = '...';
                    if (string.length > limit) {
                        string = string.substring(0, limit) + dots;
                    }

                    return string;
                }

                function formatToUsDate(date) {
                    let month = date.getMonth() + 1;
                    return [month, date.getDate(), date.getFullYear()].join('/');
                }

                function formatToEuDate(date) {
                    let month = date.getMonth() + 1;
                    return [date.getDate(), month, date.getFullYear()].join('/');
                }

                function determineWidgetLanguage(json) {
                    // list of preferred languages (order matters)
                    let arr = [json.language];
                    if (window.navigator.languages) {
                        arr = arr.concat(window.navigator.languages);
                    }
                    arr.push(window.navigator.userLanguage || window.navigator.language);

                    // pick the first one that matches
                    for (let i = 0; i < arr.length; i++) {
                        let verifiedLanguage = verifyLanguage(arr[i]);
                        if (verifiedLanguage) {
                            return verifiedLanguage;
                        }
                    }

                    // defaults to English if none match
                    return 'en';
                }

                function verifyLanguage(language) {
                    if (!language) {
                        return;
                    }
                    // try with original language
                    if (translation[language]) {
                        return language;
                    }
                    // try with 2-letter language code
                    let short = language.substring(0, 2);
                    if (translation[short]) {
                        return short;
                    }
                }

                function translate(key) {
                    return translation[widgetLanguage][key]
                        ? translation[widgetLanguage][key]
                        : key;
                }

                function restoreFilter(widgetID, key) {
                    let defaultFilterValue = widgetJSON[widgetID][key];
                    return defaultFilterValue ? defaultFilterValue : '';
                }

                translation['zh'] = translation['zh-cn'];
                return widget;
            })(window);
    });

    window.addEventListener('translationsLoaded', function () {
        widget({
            company_code: 'Solidigm',
            auto_width: 'auto',
            auto_height: 'auto',
            number: 'on',
            job_title: 'true',
            location: 'true',
            type_of_employment: 'true',
            department: 'true',
            published_since: 'true',
            dateFormat: 'us',
            add_search: 'true',
            departments_field_id: '60899b065cc16263bc9f9cf0',
            jobAdType: 'PUBLIC',
            jobs_number: '1000',
            auth: '38f0d5f7510d47a1b13ab69ea753d24b',
            trid: '998bc6c9-cfbe-4db9-af4b-d7bb8407f264',
            api_url: 'https://www.smartrecruiters.com',
        });
    });
});
