<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


 
<script>
$(function(){
    $("ul.pagination li.disabled a").click(function(){
        return false;
    });
});
 
</script>
 
<nav>
  <ul class="pagination">
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?start=0${page.param}" aria-label="Previous" >
        <span aria-hidden="true">«</span>
      </a>
    </li>
 
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?start=${page.start-page.count}${page.param}" aria-label="Previous" >
        <span aria-hidden="true">‹</span>
      </a>
    </li>    
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.start+page.count}${page.param}" aria-label="Next">
        <span aria-hidden="true">›</span>
      </a>
    </li>
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.last}${page.param}" aria-label="Next">
        <span aria-hidden="true">»</span>
      </a>
    </li>
  </ul>
</nav>