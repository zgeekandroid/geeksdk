/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.yalantis.ucrop.util;

public final class CubicEasing {

    public static float easeOut(float time, float start, float end, float duration) {
        return end * ((time = time / duration - 1.0f) * time * time + 1.0f) + start;
    }

    public static float easeIn(float time, float start, float end, float duration) {
        return end * (time /= duration) * time * time + start;
    }

    public static float easeInOut(float time, float start, float end, float duration) {
        return (time /= duration / 2.0f) < 1.0f ? end / 2.0f * time * time * time + start : end / 2.0f * ((time -= 2.0f) * time * time + 2.0f) + start;
    }

}
