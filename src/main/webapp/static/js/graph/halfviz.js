//
// halfviz.js
//
// instantiates all the helper classes, sets up the particle system + renderer
// and maintains the canvas/editor splitview
//
(function(){
  
  trace = arbor.etc.trace
  objmerge = arbor.etc.objmerge
  objcopy = arbor.etc.objcopy
  var parse = Parseur().parse

  var HalfViz = function(canvasId,dataUrl){
    sys = arbor.ParticleSystem(2600, 512, 0.5)
    // sys = arbor.ParticleSystem()
    sys.renderer = Renderer(canvasId) // our newly created renderer will have its .init() method called shortly by sys...
    sys.screenPadding(20)
    
    var _canvas = $(canvasId)
    
    var _updateTimeout = null
    var _current = null // will be the id of the doc if it's been saved before
    var _failures = null
    
    var that = {
      init:function(){
        
        $(window).resize(that.resize)
        that.resize()
        that.updateLayout(Math.max(1, $(window).width()-340))

        that.initMouseHandler()
        that.getDoc(dataUrl)
        return that
      },
      
      initMouseHandler:function(){
      
      },
      
      getDoc:function(e){
        $.getJSON(e, function(doc){

          // update the system parameters
          if (doc.sys){
            sys.parameters(doc.sys)
          }

          // modify the graph in the particle system
          that.updateGraph(doc.src)
          that.resize()
        })
        
      },

      updateGraph:function(src_txt){
        var network = parse(src_txt)
        $.each(network.nodes, function(nname, ndata){
          if (ndata.label===undefined) ndata.label = nname
        })
        sys.merge(network)
        _updateTimeout = null
      },
      
      resize:function(){        
        var w = $(window).width() - 40
        that.updateLayout(w)
        sys.renderer.redraw()
      },
      
      updateLayout:function(split){
        /*
        _canvas.width = canvW
        _canvas.height = canvH
        sys.screenSize(canvW, canvH)
                
        */
      },
      
      typing:function(e){
        var c = e.keyCode
        if ($.inArray(c, [37, 38, 39, 40, 16])>=0){
          return
        }
        
        if (_updateTimeout) clearTimeout(_updateTimeout)
        _updateTimeout = setTimeout(that.updateGraph, 900)
      }
    }
    
    return that.init()    
  }
  
  $(document).ready(function(){
    var mcp = HalfViz(GraphConfig.canvasId,GraphConfig.dataUrl)
  })
})()
