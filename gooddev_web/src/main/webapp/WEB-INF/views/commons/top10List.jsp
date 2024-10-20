<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="top10-container">
    <div class="top10-header">
        <h3>실시간 TOP10</h3>
    </div>
    <div class="top10-list">
        <table>
            <tbody>
                <c:forEach var="board" items="${topTenList}" varStatus="status">
                    <tr class="top10-item">
                        <td class="top10-rank">${status.index + 1}</td>
                        <td class="top10-title">
                            <div class="td-link">
                                <a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

	<script>
		document.addEventListener('DOMContentLoaded', function() {
			let links = document.querySelectorAll('.top10-link');
            const contextPath = "${pageContext.request.contextPath}";
            links.forEach(function(link) {
                link.addEventListener('click', function() {
                    let boardBno = link.getAttribute('data-board-bno');
                    let pageLink = link.getAttribute('data-link');
                    let page = link.getAttribute('data-page');
                    let encodedLink = encodeURIComponent(pageLink);
                    link.href = contextPath+"/board/read?bno=" + boardBno + "&link="+encodedLink;
                });
            });
        });
	</script>
