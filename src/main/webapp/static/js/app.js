
Object.byString = function(o, s) {
    s = s.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
    s = s.replace(/^\./, '');           // strip a leading dot
    var a = s.split('.');
    for (var i = 0, n = a.length; i < n; ++i) {
        var k = a[i];
        if (k in o) {
            o = o[k];
        } else {
            return;
        }
    }
    return o;
}

window.setZoom = function(zoom, instance, transformOrigin, el) {
	  transformOrigin = transformOrigin || [ 0.5, 0.5 ];
	  instance = instance || jsPlumb;
	  el = el || instance.getContainer();
	  var p = [ "webkit", "moz", "ms", "o" ],
	      s = "scale(" + zoom + ")",
	      oString = (transformOrigin[0] * 100) + "% " + (transformOrigin[1] * 100) + "%";

	  for (var i = 0; i < p.length; i++) {
	    el.style[p[i] + "Transform"] = s;
	    el.style[p[i] + "TransformOrigin"] = oString;
	  }

	  el.style["transform"] = s;
	  el.style["transformOrigin"] = oString;

	  instance.setZoom(zoom);    
};

formatNodeStr = function(nodeStr) {
	return finalStr = nodeStr.length == 1 ? finalStr = ' ' + nodeStr : nodeStr;
}

$(document).ready(function(){
	
	
	
});

jsPlumb.ready(function () {
//	var toolkit = jsPlumbToolkit.newInstance();
//	var toolkit = jsPlumbToolkit.newInstance({
//		  data:{
//		    "nodes":[
//		      { "id":"foo", "name":"foo" },
//		      { "id":"bar", "name":"bar" }
//		    ],
//		    "edges":[
//		      { "source":"foo", "target":"bar" }
//		    ]
//		  }
//		});
	

    var instance = window.jsp = jsPlumb.getInstance({
        // default drag options
        DragOptions: {cursor: 'pointer', zIndex: 2000},
        // the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
        // case it returns the 'labelText' member that we set on each connection in the 'init' method below.
        ConnectionOverlays: [
            ["Arrow", {location: 1}]
//            ["Label", {
//                    location: 0.5,
//                    id: "label",
//                    cssClass: "aLabel"
//                }]
        ],
        Container: "canvas"
    });

    var basicType = {
        connector: "Straight",
        paintStyle: {strokeStyle: "red", lineWidth: 4},
        hoverPaintStyle: {strokeStyle: "blue"},
        overlays: [
            "Arrow"
        ]
    };
    instance.registerConnectionType("basic", basicType);
    

    // this is the paint style for the connecting lines..
    var connectorPaintStyle = {
        lineWidth: 4,
        strokeStyle: "#61B7CF",
        joinstyle: "round",
        outlineColor: "white",
        outlineWidth: 2
    },
    // .. and this is the hover style.
    connectorHoverStyle = {
        lineWidth: 4,
        strokeStyle: "#216477",
        outlineWidth: 2,
        outlineColor: "white"
    },
    endpointHoverStyle = {
        fillStyle: "#216477",
        strokeStyle: "#216477"
    },
    // the definition of source endpoints (the small blue ones)
    sourceEndpoint = {
        endpoint: "Dot",
        anchor:[ "Perimeter", { shape:"Circle" } ],
        paintStyle: {
            strokeStyle: "#7AB02C",
            fillStyle: "transparent",
            radius: 7,
            lineWidth: 3
        },
        isSource: true,
        isTarget: true,
//        connector: ["Flowchart", {stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true}],
        connector: ["Straight", {stub: [40, 60], gap: 10}],
        connectorStyle: connectorPaintStyle,
        hoverPaintStyle: endpointHoverStyle,
        connectorHoverStyle: connectorHoverStyle,
        dragOptions: {},
        overlays: [
            ["Label", {
                    location: [0.5, 1.5],
                    label: "",
                    cssClass: "endpointSourceLabel"
                }]
        ]
    },
    // the definition of target endpoints (will appear when the user drags a connection)
    targetEndpoint = {
        endpoint: "Dot",
        paintStyle: {fillStyle: "#7AB02C", radius: 11},
        hoverPaintStyle: endpointHoverStyle,
        maxConnections: -1,
        dropOptions: {hoverClass: "hover", activeClass: "active"},
        isTarget: true,
        overlays: [
            ["Label", {location: [0.5, -0.5], label: "", cssClass: "endpointTargetLabel"}]
        ]
    },
    init = function (connection) {
        connection.getOverlay("label").setLabel(connection.sourceId.substring(15) + "-" + connection.targetId.substring(15));
    };
       

    var _addEndpoints = function (toId, sourceAnchors, targetAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + sourceAnchors[i];
            instance.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
        }
        for (var j = 0; j < targetAnchors.length; j++) {
            var targetUUID = toId + targetAnchors[j];
            instance.addEndpoint("flowchart" + toId, targetEndpoint, {anchor: targetAnchors[j], uuid: targetUUID});
        }
    };
    
    
    var project = JSON.parse($('#project').val());
    var projectId = $('#projectId').val();
    var userId = $('#userId').val();
//    var userId = project.user.id;
    
    var projectAsJSON = JSON.stringify(project, 'circular');
    
    var _deleteGraph = function() {
        instance.deleteEveryEndpoint();
        $("#canvas").remove();
    };
    
    var _calcGraph = function() {
    	
    	var test2 = Object.byString(project, 'activities[0].gprSuccessors[0].rightActivity');
    	var test3 = Object.byString(project, 'activities[0].gprSuccessors[0].leftActivity');
    	var test4 = Object.byString(project, 'activities[0].compressionCosts[0].compressionCost');
    	var test5 = Object.byString(project, 'activities[0].compressionCosts[0].durationLB');
    	var test6 = Object.byString(project, 'activities[0].compressionCosts[0].durationUB');
    	
    	_deleteGraph();
    	
    	//TODO: need a sort of activity array to ensure they are lexographically ordered
    	var activities = project.activities;
    	var div1 = $("<div></div>").attr('id','canvas').addClass("wrap").addClass("jtk-demo-canvas").addClass("canvas-wide").addClass("flowchart-demo").addClass("jtk-surface");
    	div1.appendTo('body'); 
    	instance.setContainer("canvas");
    	
    	var gprs = [];
    	for (var i=0; i<activities.length; i++) {
            var activity = activities[i];
            var gprPreds = activity.gprPredecessors;
            if (gprPreds.length>0 && i==0) {
        		alert("error - must have a single start activity at index zero with no predecessors")
            	break;
            }
            
          //workaround the problem that gprPredecessors are not serializing properly from Java by erasing them and re-adding them in script
            activity.gprPredecessors = [];
            activities[i].gprPredecessors = [];
//            if (i>0) {
//        		alert("error - must have a single start activity at index zero with no predecessors")
//            	break;
//            }
            var gprSucc = activity.gprSuccessors;
            for(var j=0; j<gprSucc.length; j++) {
            	var gpr = gprSucc[j];
            	gprs.push(gpr);
            }
        }
    	
    	for(var j=0; j<gprs.length; j++) {
        	var gpr = gprs[j];
        	var type = gpr.gprType;
        	var delta = gpr.delta;
        	var leftActivityName = typeof gpr.leftActivity === 'object' ? gpr.leftActivity.activityName : gpr.leftActivity;
        	var leftActivity = activities.filter(function( obj ) {
    		  return obj.activityName == leftActivityName;
        	});
        	gpr.leftActivity = leftActivity[0];
        	var leftDuration = leftActivity[0].durations[0];
        	var rightActivityName = typeof gpr.rightActivity === 'object' ? gpr.rightActivity.activityName : gpr.rightActivity;
        	var rightActivity = activities.filter(function( obj ) {
      		  return obj.activityName == rightActivityName;
      		});
        	gpr.rightActivity = rightActivity[0];
        	rightActivity[0].gprPredecessors.push(gpr);
        	var rightDuration = rightActivity[0].durations[0];
        	var gprLength;
        	if (type == 'FS') {
        		gprLength = delta
        	} else if (type == 'SS') {
        		gprLength = delta - leftDuration;
        	} else if (type == 'FF') {
        		gprLength = delta - rightDuration;
        	} else if (type == 'SF') {
        		gprLength = delta - leftDuration - rightDuration;
        	}
        	gpr.length = gprLength;
        	gprLength = +gprLength.toFixed(2);       
    	}
    	
    	var activity = activities[0];
    	activity.startTime = 0;
    	activity.endTime = activity.startTime + activity.durations[0];
    	activity.startNode = 1;
    	activity.endNode = 2;
    	
    	
    	//TODO: we must ensure that every project has a single start and terminal activity and if not add appropriate dummy activities if needed
    	for (var i = 0; i<activities.length; i++) {
    		var activity = activities[i];
            var name = activity.activityName;
    		var gprPred = activity.gprPredecessors;
    		var maxVal = -1e7;
    		if (i>0) {
    			activity.startNode = 2*i+1;
    	    	activity.endNode = 2*i+2;
    			for (var j=0; j<gprPred.length; j++) {
                	var gpr = gprPred[j];
                	var leftActivity = activities.filter(function( obj ) {
                		return obj.activityName == gpr.leftActivity.activityName;
                	});
                	var val = leftActivity[0].endTime + gpr.length;
                	if (val > maxVal) {
    					maxVal = val;
    				}
                }
                activity.startTime = maxVal;
                activity.endTime = activity.startTime + activity.durations[0];
    		}
            var durations = activity.durations;
            var duration = +durations[0].toFixed(2);    //round double value to 2 decimal places but leave off trailing zeroes
            var costs = activity.compressionCosts;
            var cost = +costs[0].compressionCost.toFixed(2);
            var table = $("<table></table>").attr('id',name).addClass("window").addClass("jtk-node");
        	var row1 = $("<tr></tr>");
        	var row2 = $("<tr></tr>");
        	var row3 = $("<tr></tr>");
        	var td1r1 = $("<td></td>").attr('id',name + '_startNode').text(formatNodeStr(activity.startNode)).data('activity', name);
        	var td2r1 = $("<td></td>").attr('id',name + '_cost').text('$' + cost).data('activity', name).addClass("editable");
        	var td3r1 = $("<td></td>").attr('id',name + '_endNode').text(formatNodeStr(activity.endNode)).data('activity', name);
        	var row1 = row1.append(td1r1,td2r1,td3r1);
        	var tdr2 = $("<td></td>").attr('id',name + '_activityName').attr('colspan','3').addClass("midRow").text(name).data('activity', name);
        	var row2 = row2.append(tdr2);
        	var td1r3 = $("<td></td>").attr('id',name + '_startTime').text(activity.startTime).data('activity', name);
        	var td2r3 = $("<td></td>").attr('id',name + '_duration').text(duration).data('activity', name).addClass("editable");
        	var td3r3 = $("<td></td>").attr('id',name + '_endTime').text(activity.endTime).data('activity', name);
        	var row3 = row3.append(td1r3,td2r3,td3r3);
        	table.append(row1,row2,row3);
        	table.appendTo(div1);
		}
		var makespan = activity.endTime;
		
		for (var j=0; j<gprs.length; j++) {
			var gpr = gprs[j];
			var type = gpr.gprType;
        	var delta = gpr.delta;
			var conn = instance.connect({
	            source:gpr.leftActivity.activityName,
	            target:gpr.rightActivity.activityName,
//	            Endpoints : [ [ "Dot", { radius:2 } ], [ "Dot", { radius:2 } ] ],
	            endpoint: "Dot",
	            endpointStyle:{radius:5},
	            anchor:"Continuous",
	            paintStyle: {
	                strokeStyle: "#7AB02C",
	                fillStyle: "transparent",
	                radius: 2,
	                lineWidth: 3
	            },
	            connector: ["Straight", {stub: [40, 60], gap: 0}],
	            newConnection:true,
	            
	            connectorStyle: connectorPaintStyle,
	            hoverPaintStyle: endpointHoverStyle,
	            connectorHoverStyle: connectorHoverStyle,
	            dragOptions: {},
	            overlays: [
	                       ["Label", {
	                               location: 0.35,
	                               id: "label",
	                               cssClass: "aLabel",
	                               events:{
	                                   click:function(labelOverlay, originalEvent) { 
	                                     console.log("click on label overlay for :" + labelOverlay.component); 
	                                   }
	                                 }
	                           }]
	                   ]
	        });
	    	
	    	var label = conn.getOverlay("label");
	    	var name = leftActivity[0].activityName;
	    	label.setLabel(type + " (" + +delta.toFixed(2) + ")");
		}
		
		
		$('#canvas').delegate('td.editable', 'dblclick', function(event) {
    		var OriginalContent = $(this).text();
			var tdCell = event.target;
			$(this).addClass("cellEditing");
			$(this).html("<input type='text' value='" + OriginalContent + "' />");
			$(this).children().first().focus();
			$(this).children().first().select();
			$(this).children().first().keypress(function(e) {
				if (e.which == 13) {
					var newContent = $(this).val();
					$(this).parent().text(newContent);
					$(this).parent().removeClass("cellEditing");
					var idStr = tdCell.id;
					var res = idStr.toString().split("_");
					var name = res[0];
					var cell = res[1];
					var activity = project.activities.filter(function( obj ) {
			    		  return obj.activityName == name;
			        });
					var d = Number(newContent);
					
					switch (cell) {
				    case "startNode":
				        // Blah
				        break;
				    case "cost":
				    	if (d >= 0) {
				    		activity[0].compressionCosts[0].compressionCost = +d.toFixed(2);
				    		instance.batch(function () {
				    			_calcGraph();
				    			createInput();
				    			saveProject();
				    		});
				    	} else {
				    		//undo change
				    	}		
				        break;
				    case "endNode":
				        // Blah
				        break;
				    case "startTime":
				        break;
				    case "duration":
				    	if (d >= 0) {
				    		activity[0].durations[0] = d;
				    		instance.batch(function () {
				    			_calcGraph();
				    			createInput();
				    			saveProject();
				    		});
				    	} else {
				    		//undo change
				    	}				    	
				        break;
				    case "endTime":
				        // Blah
				        break;
				    case "activityName":
				        // Blah
				        break;
				}

		
				}
			});
			$(this).children().first().blur(function() {
				$(this).parent().text(OriginalContent);
				$(this).parent().removeClass("cellEditing");
			});
    		
    	});
		
		
		// listen for new connections; initialise them the same way we initialise the connections at startup.
        instance.bind("connection", function (connInfo, originalEvent) {
            init(connInfo.connection);
        });
		
		
		// make all the window divs draggable
        instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), {grid: [20, 20]});
        // THIS DEMO ONLY USES getSelector FOR CONVENIENCE. Use your library's appropriate selector
        // method, or document.querySelectorAll:
        //jsPlumb.draggable(document.querySelectorAll(".window"), { grid: [20, 20] });

       
        // listen for clicks on connections, and offer to delete connections on click.
        //
        instance.bind("click", function (conn, originalEvent) {
            // if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
            //   instance.detach(conn);
            conn.toggleType("basic");
        });

        instance.bind("connectionDrag", function (connection) {
            console.log("connection " + connection.id + " is being dragged. suspendedElement is ", connection.suspendedElement, " of type ", connection.suspendedElementType);
        });

        instance.bind("connectionDragStop", function (connection) {
            console.log("connection " + connection.id + " was dragged");
        });

        instance.bind("connectionMoved", function (params) {
            console.log("connection " + params.connection.id + " was moved");
        });
        
        //listen for clicks on endpoints (activities)
        instance.bind("endpointClick", function(endpoint, originalEvent) {
            console.log("you clicked on ", endpoint);
        });


        // construct dagre graph from JsPlumb graph
        var g = new dagre.graphlib.Graph();
        g.setGraph({});
        g.setDefaultEdgeLabel(function () {
            return {};
        });

        var nodes = $(".jtk-node");
        nodes.each(function (i, el) {
            var node = $(el);
            var nodeId = node.attr('id');
            var nodeWidth = node.width();
            var nodeHeight = node.height();
            g.setNode(nodeId, {width: nodeWidth, height: nodeHeight});
        });

        var edges = instance.getAllConnections();
        for (var i = 0; i < edges.length; i++) {
            var c = edges[i];
            g.setEdge(c.source.id, c.target.id);
        }

        // calculate the layout (i.e. node positions)
        dagre.layout(g);
        // Applying the calculated layout
        g.nodes().forEach(function (v) {
            $("#" + v).css("left", 1.5*g.node(v).x + "px");
            $("#" + v).css("top", 1.5*g.node(v).y + "px");
        });
        
        instance.repaintEverything();

	    jsPlumb.fire("jsPlumbDemoLoaded", instance);
	    var container = instance.getContainer();
	    window.setZoom(1.5, instance, [ 0, 0 ], container);
    };

    instance.batch(function () {
    	_calcGraph();
    	
    });
    
    function createInput() {
    	if($('#btnProjectSave').length == 0) {
    		 var $input = $('<input type="button" value="Save Project" />').attr('id','btnProjectSave');
    	     $input.appendTo($("body"));
    	     $('#btnProjectSave').show;
    	     $('#canvas').delegate('td.editable', 'dblclick', function(event) {
    	    	 saveProject();
    	     });
    	}
    }
    
    function removeCircularRef() {
    	
    	var activities = project.activities;
    	var gprs = [];
    	
    	for (var i=0; i<activities.length; i++) {
            var activity = activities[i];
            var gprPreds = activity.gprPredecessors;
            if (gprPreds.length>0 && i==0) {
        		alert("error - must have a single start activity at index zero with no predecessors")
            	break;
            }
            
          //workaround the problem that gprPredecessors are not serializing properly from Java by erasing them and re-adding them in script
            activity.gprPredecessors = [];
            activities[i].gprPredecessors = [];
//            if (i>0) {
//        		alert("error - must have a single start activity at index zero with no predecessors")
//            	break;
//            }
            var gprSucc = activity.gprSuccessors;
            for(var j=0; j<gprSucc.length; j++) {
            	var gpr = gprSucc[j];
            	gprs.push(gpr);
            }
        }
    	
    	for(var j=0; j<gprs.length; j++) {
        	var gpr = gprs[j];
        	var type = gpr.gprType;
        	var delta = gpr.delta;
        	var leftActivityName = typeof gpr.leftActivity === 'object' ? gpr.leftActivity.activityName : gpr.leftActivity;
        	var leftActivity = activities.filter(function( obj ) {
    		  return obj.activityName == leftActivityName;
        	});
//        	gpr.leftActivity = leftActivity[0].activityName;
        	gpr.leftActivity = leftActivity[0].activityId;
        	var rightActivityName = typeof gpr.rightActivity === 'object' ? gpr.rightActivity.activityName : gpr.rightActivity;
        	var rightActivity = activities.filter(function( obj ) {
      		  return obj.activityName == rightActivityName;
      		});
//        	gpr.rightActivity = rightActivity[0].activityName;  
        	gpr.rightActivity = rightActivity[0].activityId; 
    	}
    	
    }
    
//    function saveProject() {
//    	removeCircularRef();
//    	var projJSON = JSON.stringify(project);
//        $.ajax({
//		    url: '/OpNet/update-project-' + userId + '-' + projectId,
//		    data: ({name : projJSON}),
//		    success: function(data, textStatus ){
//			console.log(data);
//				alert("success");
//			},
//			error: function(xhr, textStatus, errorThrown){
//				alert('request failed'+errorThrown);
//			}
//	    });
//    }
    
    function saveProject() {
    	removeCircularRef();
    	removeTransientFields(project);
    	var projJSON = JSON.stringify(project);
        $.post('/OpNet/update-project-' + userId + '-' + projectId, {
    		proj : projJSON
    	}, function(data) {

    		var json = JSON.parse(data);
    		//...

    	}).done(function() {
    	}).fail(function(xhr, textStatus, errorThrown) {
    	}).complete(function() {		
    	});
    }
    
    
    function removeTransientFields(project) {
    	
    	var p = project;
    	delete p['@projectId'];
    	
    	for(var i=0; i < p.activities.length; i++) {
    		var a = p.activities[i];
    		var activityId = a.activityId;
    		delete a['@activityId'];
    		delete a['durations'];
        	delete a['sampleSize'];
        	delete a['startNode'];
        	delete a['endNode'];
        	delete a['startTime'];
        	delete a['endTime'];
        	delete a['durationDistribution.@durationDistId'];
        	a.durationDistribution.activity = activityId;
        	for(var j=0; j < a.gprSuccessors.length; j++) {
        		var g = a.gprSuccessors[j];
        		g.leftActivity = activityId;
        		delete g['@gprId'];
        	}
        	for(var j=0; j < a.gprPredecessors.length; j++) {
        		var g = a.gprPredecessors[j];
        		g.rightActivity = activityId;
        		delete g['@gprId'];
        	}
        	for(var j=0; j < a.compressionCosts.length; j++) {
        		var c = a.compressionCosts[j];
        		c.activity = activityId;
        		delete c['@costId'];
        		delete c['activity'];
        	}
    	}
    		
    }
    	
    	
    	
    
    
    
    
//    function saveProject() {
//       var projJSON = JSON.stringify(project, 'circular');
//       removeCircularRef();
//	   $.ajax({
//		   type: 'POST',
//	    	dataType: 'json',
//	    	contentType:'application/json',
//	    	url: '/OpNet/update-project-' + userId + '-' + projectId,
//	    	data:'project=' + JSON.stringify(project, 'circular'),
//	    	success: function(data, textStatus ){
//	    	console.log(data);
//	    		alert("success");
//	    	},
//	    	error: function(xhr, textStatus, errorThrown){
//	    		alert('request failed'+errorThrown);
//	    	}
//	  });
//	}
   
});