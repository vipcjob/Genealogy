package com.fcz.genealogy.ui.familytree.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.fcz.genealogy.R
import com.fcz.genealogy.data.model.FamilyMember
import com.fcz.genealogy.data.model.Gender
import com.fcz.genealogy.util.DateUtils

/**
 * 自定义族谱树视图，用于绘制族谱树状图
 */
class FamilyTreeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    // 成员节点配置
    private val nodeWidth = dpToPx(180f)
    private val nodeHeight = dpToPx(80f)
    private val nodeMarginX = dpToPx(20f)
    private val nodeMarginY = dpToPx(40f)
    private val cornerRadius = dpToPx(4f)
    
    // 字体配置
    private val nameTextSize = spToPx(16f)
    private val detailTextSize = spToPx(12f)
    private val genTagTextSize = spToPx(10f)
    
    // 颜色配置
    private val maleColor = ContextCompat.getColor(context, R.color.male)
    private val femaleColor = ContextCompat.getColor(context, R.color.female)
    private val focusColor = ContextCompat.getColor(context, R.color.primary)
    private val lineColor = ContextCompat.getColor(context, R.color.gray)
    private val statusColorLiving = ContextCompat.getColor(context, R.color.status_living)
    private val statusColorDeceased = ContextCompat.getColor(context, R.color.status_deceased)
    
    // 线条配置
    private val lineWidth = dpToPx(1.5f)
    
    // 数据
    private var focusMember: FamilyMember? = null
    private var ancestors: List<FamilyMember> = emptyList()
    private var descendants: List<FamilyMember> = emptyList()
    
    // 计算布局
    private val nodeMap = mutableMapOf<String, Rect>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private var onMemberClickListener: ((FamilyMember) -> Unit)? = null
    
    // 设置焦点成员
    fun setFocusMember(member: FamilyMember) {
        this.focusMember = member
    }
    
    // 设置祖先
    fun setAncestors(ancestors: List<FamilyMember>) {
        this.ancestors = ancestors
    }
    
    // 设置后代
    fun setDescendants(descendants: List<FamilyMember>) {
        this.descendants = descendants
    }
    
    // 设置成员点击监听器
    fun setOnMemberClickListener(listener: (FamilyMember) -> Unit) {
        this.onMemberClickListener = listener
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = calculateTreeWidth()
        val height = calculateTreeHeight()
        
        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateNodePositions()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (focusMember == null) return
        
        // 绘制连接线
        drawLines(canvas)
        
        // 绘制所有节点
        drawNodes(canvas)
    }
    
    private fun drawNodes(canvas: Canvas) {
        // 绘制祖先节点
        for (ancestor in ancestors) {
            drawMemberNode(canvas, ancestor, false)
        }
        
        // 绘制焦点成员节点
        focusMember?.let { drawMemberNode(canvas, it, true) }
        
        // 绘制后代节点
        for (descendant in descendants) {
            drawMemberNode(canvas, descendant, false)
        }
    }
    
    private fun drawMemberNode(canvas: Canvas, member: FamilyMember, isFocus: Boolean) {
        val rect = nodeMap[member.id] ?: return
        
        // 绘制节点背景
        paint.reset()
        paint.isAntiAlias = true
        
        // 根据性别和焦点状态设置颜色
        val bgColor = when {
            isFocus -> focusColor
            member.gender == Gender.MALE -> maleColor
            else -> femaleColor
        }
        
        paint.color = bgColor
        paint.style = Paint.Style.FILL
        
        rectF.set(rect)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        
        // 绘制边框
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(1f)
        paint.color = Color.WHITE
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        
        // 绘制姓名
        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = nameTextSize
        paint.textAlign = Paint.Align.CENTER
        
        val nameY = rect.top + dpToPx(20f)
        canvas.drawText(member.name, rect.centerX().toFloat(), nameY, paint)
        
        // 绘制世代标签
        val genText = "第${member.generation}代"
        paint.textSize = genTagTextSize
        val genY = rect.top + dpToPx(38f)
        canvas.drawText(genText, rect.centerX().toFloat(), genY, paint)
        
        // 绘制生存状态
        val statusY = rect.top + dpToPx(58f)
        if (member.isAlive) {
            paint.color = statusColorLiving
            canvas.drawText("在世", rect.centerX().toFloat(), statusY, paint)
        } else {
            paint.color = statusColorDeceased
            canvas.drawText("已故", rect.centerX().toFloat(), statusY, paint)
        }
    }
    
    private fun drawLines(canvas: Canvas) {
        paint.reset()
        paint.isAntiAlias = true
        paint.color = lineColor
        paint.strokeWidth = lineWidth
        paint.style = Paint.Style.STROKE
        
        // 绘制祖先连接线
        drawAncestorLines(canvas)
        
        // 绘制后代连接线
        drawDescendantLines(canvas)
    }
    
    private fun drawAncestorLines(canvas: Canvas) {
        if (ancestors.isEmpty() || focusMember == null) return
        
        var childId = focusMember!!.id
        
        for (ancestor in ancestors) {
            val parentRect = nodeMap[ancestor.id] ?: continue
            val childRect = nodeMap[childId] ?: continue
            
            // 垂直连接线
            val startX = parentRect.centerX().toFloat()
            val startY = parentRect.bottom.toFloat()
            val endX = childRect.centerX().toFloat()
            val midY = (startY + childRect.top) / 2
            
            // 绘制三段线
            val path = Path()
            path.moveTo(startX, startY)
            path.lineTo(startX, midY)
            path.lineTo(endX, midY)
            path.lineTo(endX, childRect.top.toFloat())
            
            canvas.drawPath(path, paint)
            
            childId = ancestor.id
        }
    }
    
    private fun drawDescendantLines(canvas: Canvas) {
        if (focusMember == null) return
        
        val parentMap = createParentChildMap()
        drawChildrenLines(canvas, focusMember!!.id, parentMap)
    }
    
    private fun drawChildrenLines(canvas: Canvas, parentId: String, parentChildMap: Map<String, List<String>>) {
        val children = parentChildMap[parentId] ?: return
        if (children.isEmpty()) return
        
        val parentRect = nodeMap[parentId] ?: return
        
        // 如果只有一个孩子，直接垂直连接
        if (children.size == 1) {
            val childRect = nodeMap[children[0]] ?: return
            
            val startX = parentRect.centerX().toFloat()
            val startY = parentRect.bottom.toFloat()
            val endX = childRect.centerX().toFloat()
            val endY = childRect.top.toFloat()
            val midY = (startY + endY) / 2
            
            val path = Path()
            path.moveTo(startX, startY)
            path.lineTo(startX, midY)
            path.lineTo(endX, midY)
            path.lineTo(endX, endY)
            
            canvas.drawPath(path, paint)
        } else {
            // 有多个孩子，绘制树状结构
            val firstChildRect = nodeMap[children.first()] ?: return
            val lastChildRect = nodeMap[children.last()] ?: return
            
            val startX = parentRect.centerX().toFloat()
            val startY = parentRect.bottom.toFloat()
            val leftX = firstChildRect.centerX().toFloat()
            val rightX = lastChildRect.centerX().toFloat()
            val midY = startY + nodeMarginY / 2
            
            // 绘制父节点到中间的垂直线
            canvas.drawLine(startX, startY, startX, midY, paint)
            
            // 绘制横向连接线
            canvas.drawLine(leftX, midY, rightX, midY, paint)
            
            // 绘制每个孩子的垂直连接线
            for (childId in children) {
                val childRect = nodeMap[childId] ?: continue
                val childX = childRect.centerX().toFloat()
                val childY = childRect.top.toFloat()
                
                canvas.drawLine(childX, midY, childX, childY, paint)
            }
        }
        
        // 递归绘制每个孩子的后代
        for (childId in children) {
            drawChildrenLines(canvas, childId, parentChildMap)
        }
    }
    
    private fun createParentChildMap(): Map<String, List<String>> {
        val parentChildMap = mutableMapOf<String, MutableList<String>>()
        
        for (member in descendants) {
            if (member.parentId != null) {
                val children = parentChildMap.getOrPut(member.parentId!!) { mutableListOf() }
                children.add(member.id)
            }
        }
        
        // 添加焦点成员的直接子女
        if (focusMember != null) {
            val directChildren = descendants.filter { it.parentId == focusMember!!.id }
            parentChildMap[focusMember!!.id] = directChildren.map { it.id }.toMutableList()
        }
        
        return parentChildMap
    }
    
    private fun calculateNodePositions() {
        nodeMap.clear()
        
        val viewCenterX = width / 2
        val startY = dpToPx(20f)
        
        // 计算祖先节点位置
        var currentY = startY
        for (i in ancestors.size - 1 downTo 0) {
            val ancestor = ancestors[i]
            val nodeRect = Rect(
                viewCenterX - (nodeWidth / 2).toInt(),
                currentY.toInt(),
                viewCenterX + (nodeWidth / 2).toInt(),
                (currentY + nodeHeight).toInt()
            )
            nodeMap[ancestor.id] = nodeRect
            currentY += nodeHeight + nodeMarginY
        }
        
        // 计算焦点成员节点位置
        if (focusMember != null) {
            val focusRect = Rect(
                viewCenterX - (nodeWidth / 2).toInt(),
                currentY.toInt(),
                viewCenterX + (nodeWidth / 2).toInt(),
                (currentY + nodeHeight).toInt()
            )
            nodeMap[focusMember!!.id] = focusRect
            currentY += nodeHeight + nodeMarginY
        }
        
        // 计算后代节点位置
        if (focusMember != null) {
            calculateDescendantPositions(focusMember!!.id, viewCenterX.toFloat(), currentY)
        }
    }
    
    private fun calculateDescendantPositions(parentId: String, parentCenterX: Float, startY: Float): Float {
        val directChildren = descendants.filter { it.parentId == parentId }
        if (directChildren.isEmpty()) return 0f
        
        var currentY = startY
        var totalWidth = 0f
        
        // 计算每个孩子的位置
        for (child in directChildren) {
            val childCenterX = parentCenterX + (directChildren.indexOf(child) - directChildren.size / 2f) * (nodeWidth + nodeMarginX)
            
            val nodeRect = Rect(
                (childCenterX - nodeWidth / 2).toInt(),
                currentY.toInt(),
                (childCenterX + nodeWidth / 2).toInt(),
                (currentY + nodeHeight).toInt()
            )
            nodeMap[child.id] = nodeRect
            
            // 递归计算子节点的后代位置
            val descendantHeight = calculateDescendantPositions(child.id, childCenterX, currentY + nodeHeight + nodeMarginY)
            totalWidth += nodeWidth + nodeMarginX
            
            // 如果有后代，考虑其高度
            if (descendantHeight > 0) {
                currentY = Math.max(currentY, descendantHeight)
            }
        }
        
        return currentY + nodeHeight + nodeMarginY
    }
    
    private fun calculateTreeWidth(): Int {
        val minWidth = minOf(2000, context.resources.displayMetrics.widthPixels)
        val nodesInRow = Math.max(6, descendants.groupBy { it.generation }.maxOfOrNull { it.value.size } ?: 0)
        return Math.max(minWidth, (nodesInRow * (nodeWidth + nodeMarginX)).toInt())
    }
    
    private fun calculateTreeHeight(): Int {
        val generations = descendants.groupBy { it.generation }.size +
                ancestors.size + (if (focusMember != null) 1 else 0)
        return (generations * (nodeHeight + nodeMarginY) + dpToPx(100f)).toInt()
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val x = event.x
            val y = event.y
            
            // 检查是否点击了某个成员节点
            for ((id, rect) in nodeMap) {
                if (rect.contains(x.toInt(), y.toInt())) {
                    // 查找成员
                    val member = when {
                        focusMember != null && focusMember!!.id == id -> focusMember
                        else -> ancestors.find { it.id == id } ?: descendants.find { it.id == id }
                    }
                    
                    // 触发点击事件
                    if (member != null) {
                        onMemberClickListener?.invoke(member)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
    
    private fun dpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
    
    private fun spToPx(sp: Float): Float {
        return sp * context.resources.displayMetrics.scaledDensity
    }
} 