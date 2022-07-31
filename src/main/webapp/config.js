CKEDITOR.addCss('.cke_editable img { width: 100% !important; height: 100% !important; }');

CKEDITOR.editorConfig = function(config) {
	//config.resize_enabled = false;
	//config.removePlugins = 'resize';
    config.extraPlugins = 'codesnippet';
	config.toolbar = [ ['Bold', 'Italic', 'NumberedList', 'BulletedList', 'Link', 'Unlink', 'Image', 'Undo', 'Redo', 'codesnippet'] ];
    config.removePlugins = 'exportpdf';
    config.height = '8rem';
    //config.editorplaceholder = 'Nội dung bài viết…';
    config.contentsCss = ['body{margin: 0 2rem;}','p{font-size: 1rem;font-family: Arial, Helvetica, sans-serif;}'];
	/*config.toolbar_Complex = [
			[ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
					'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
					'Paste', 'Link', 'Unlink', 'Image'],
			[ 'Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
					'JustifyRight', 'JustifyBlock' ],
			[ 'Table', 'Smiley', 'SpecialChar', 'PageBreak',
					'Styles', 'Format', 'Font', 'FontSize', 'Maximize'] ]; */
    //locale
    config.language = 'vi';
};

CKEDITOR.on( 'dialogDefinition', function( ev )
   {
      var dialogName = ev.data.name;
      var dialogDefinition = ev.data.definition;

      if ( dialogName == 'image' ) {
         dialogDefinition.removeContents( 'Link' );
         dialogDefinition.removeContents( 'advanced' );
         var infoTab = dialogDefinition.getContents( 'info' );
         infoTab.remove( 'txtAlt' );
         infoTab.remove( 'txtBorder' );
         infoTab.remove( 'txtHSpace' );
         infoTab.remove( 'txtVSpace' );
         //infoTab.remove( 'htmlPreview' );
         infoTab.remove( 'btnResetSize' );
         infoTab.remove( 'cmbAlign' );
         infoTab.remove( 'ratioLock' );
         
         var onOK = dialogDefinition.onOK;

         dialogDefinition.onOK = function (e) {
            var width = this.getContentElement('info', 'txtWidth');
            width.setValue('100%');//Set Default Width

            var height = this.getContentElement('info', 'txtHeight');
            height.setValue('100%');////Set Default height

            onOK && onOK.apply(this, e);
        };
      }
   });
   
   
   
   CKEDITOR.editor.prototype.resize = function(a, b, d, k) {
                        var h = this.container,
                        k = k ? this.container.getFirst(function(a) {
                            return a.type == CKEDITOR.NODE_ELEMENT && a.hasClass("cke_inner")
                        }) : h;
                        if (a || 0 === a)
                            a = CKEDITOR.tools.convertToPx(CKEDITOR.tools.cssLength(a));
                        k.setSize("width", a, !0);
                        b = CKEDITOR.tools.convertToPx(CKEDITOR.tools.cssLength(b));
                        var g = (k.$.offsetHeight || 0)
                          , h = Math.max(b - (d ? 0 : g), 0);
                        b = d ? b + g : b;
                        this.fire("resize", {
                            outerHeight: b,
                            contentsHeight: h,
                            outerWidth: a || k.getSize("width")
                        })
                    }
                    ;