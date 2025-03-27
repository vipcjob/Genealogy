package com.fcz.genealogy.ui.helper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fcz.genealogy.R
import com.fcz.genealogy.databinding.ActivityFragmentContainerBinding

/**
 * 通用的Fragment容器Activity，用于替代Navigation组件
 * 可以加载任何Fragment
 */
class FragmentContainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentContainerBinding

    companion object {
        const val EXTRA_FRAGMENT_CLASS = "extra_fragment_class"
        const val EXTRA_FRAGMENT_ARGS = "extra_fragment_args"
        const val EXTRA_TITLE = "extra_title"

        /**
         * 创建启动Fragment的Intent
         * @param context 上下文
         * @param fragmentClass 要启动的Fragment类
         * @param args Fragment参数
         * @param title 标题
         */
        inline fun <reified T : Fragment> newIntent(
            context: Context,
            args: Bundle? = null,
            title: String? = null
        ): Intent {
            return Intent(context, FragmentContainerActivity::class.java).apply {
                putExtra(EXTRA_FRAGMENT_CLASS, T::class.java.name)
                if (args != null) {
                    putExtra(EXTRA_FRAGMENT_ARGS, args)
                }
                if (title != null) {
                    putExtra(EXTRA_TITLE, title)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置标题
        intent.getStringExtra(EXTRA_TITLE)?.let {
            binding.toolbar.title = it
        }

        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // 如果是首次创建，加载指定的Fragment
        if (savedInstanceState == null) {
            val fragmentClassName = intent.getStringExtra(EXTRA_FRAGMENT_CLASS)
            val fragmentArgs = intent.getBundleExtra(EXTRA_FRAGMENT_ARGS)

            if (fragmentClassName != null) {
                try {
                    val fragmentClass = Class.forName(fragmentClassName)
                    val fragment = fragmentClass.newInstance() as Fragment
                    
                    // 设置参数
                    if (fragmentArgs != null) {
                        fragment.arguments = fragmentArgs
                    }
                    
                    // 加载Fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                } catch (e: Exception) {
                    e.printStackTrace()
                    finish()
                }
            } else {
                finish()
            }
        }
    }
} 