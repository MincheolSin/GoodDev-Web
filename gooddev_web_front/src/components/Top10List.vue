<template>
    <div class="top10-container">
        <div class="top10-header">
            <h3>실시간 TOP10</h3>
        </div>
        <div class="top10-list">
            <table>
                <tbody>
                    <tr class="top10-item" v-for="(board,index) in topTenList">
                        <td class="top10-rank">{{ index + 1 }}</td>
                        <td class="td-link">
                            <a @click.stop.prevent="$emit('topTenRead',board.bno)">{{ board.title }}</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>

const props = defineProps({
    topTenList: {
        type: Object,
        default:{},
    },
});

const emit = defineEmits(['topTenRead']);

</script>