/* 
 * Copyright (C) 2019 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

window['photohosting'] = window['photohosting'] || {};

photohosting.Slideshow = function($) {
	
	var self = this;
	
	var container = $('#background-slideshow');
	
	/**
	 * 
	 * @returns {jQuery}
	 */
	function getCurrentActive() {
		return $('.background-slide:visible', container);
	}
	
	/**
	 * 
	 * @returns {jQuery}
	 */
	function getNextActive() {
		var slide = getCurrentActive().next();
		
		if (slide.length === 0) {
			slide = $('.background-slide:first', container);
		}
		
		return slide;
	}
	
	/**
	 * Advance the slideshow to the next image.
	 * 
	 * @returns {photohosting.Slideshow}
	 */
	function advance() {
		var current = getCurrentActive(), next = getNextActive();
		
		current.fadeOut(1000);
		next.fadeIn(1000, function() {
			setTimeout(advance, 5000);
		});
		
		return self;
	}
	
	this.start = function() {
		// only slide if there is more than a single slide
		if ($('.background-slide').length > 1) {
			setTimeout(advance, 5000);
		}
	};
	
};

jQuery(function($) {
	var instance = new photohosting.Slideshow($);
	
	$(document).data('slideshow-handler', instance);
	
	instance.start();
});