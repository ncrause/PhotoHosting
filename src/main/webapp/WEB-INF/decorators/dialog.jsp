<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<section class="modal" tabindex="-1" role="dialog" id="<decorator:getProperty property="page.modalID"/>">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<header class="modal-header">
				<h5 class="modal-title">
					<decorator:getProperty property="page.header"/>
				</h5>
				
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</header>
			
			<main class="modal-body">
				<decorator:getProperty property="page.main"/>
			</main>
			
			<footer class="modal-footer">
				<decorator:getProperty property="page.footer" default="<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>"/>
			</footer>
		</div>
	</div>
</section>